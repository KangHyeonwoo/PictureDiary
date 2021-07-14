package com.picture.diary.extract.util;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import org.apache.commons.imaging.formats.jpeg.JpegUtils;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcParser;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import com.picture.diary.extract.data.PicturePathProperties;
import com.picture.diary.picture.data.PictureDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PictureExtractUtils {
	
	private final PicturePathProperties picturePathProperties;
	
	private static final SegmentFilter PHOTOSHOP_APP13_SEGMENT_FILTER = segment -> segment.isPhotoshopApp13Segment();
	
	public boolean removeDuplicatedApp13SegMents(String path) {
		return this.removeDuplicatedApp13SegMentsExcludeIndex(path, 0);
	}
	
	/**
	 *  App13 Segment 삭제
	 *  
	 * @param pictureDto
	 * @param excludeSegmentsIndex
	 * @throws ImageReadException
	 * @throws IndexOutOfBoundsException
	 * @throws IOException
	 */
	public boolean removeDuplicatedApp13SegMentsExcludeIndex(String path, int excludeSegmentsIndex) {
		final String tempPath = FilenameUtils.getBaseName(path) + "_temp." + FilenameUtils.getExtension(path);
		
		File src = new File(path);
		try (FileOutputStream fos = new FileOutputStream(new File(tempPath));
	            OutputStream os = new BufferedOutputStream(fos)){
			
			final ByteSource byteSource = new ByteSourceFile(src);
			final JFIFPieces jfifPieces = this.analyzeJFIF(byteSource);
	        final List<JFIFPiece> oldPieces = jfifPieces.pieces;
	        final List<JFIFPiece> photoshopApp13Segments = this.findPhotoshopApp13Segments(oldPieces);
	        
	        if(photoshopApp13Segments.size() == 1) {
	        	return false;
	        }
	        
	        for(JFIFPiece piece : photoshopApp13Segments) {
	        	oldPieces.remove(piece);        	
	        }
	        
	        oldPieces.add(photoshopApp13Segments.get(excludeSegmentsIndex));
			
			this.writeSegments(os, oldPieces);

			Files.delete(Paths.get(path));
			Files.move(Paths.get(tempPath), Paths.get(path));
			
		} catch (IndexOutOfBoundsException e) {
			return false;
		} catch (FileNotFoundException e) {
			log.error("Photoshop App13 Segment failed to remove. Not found File. File path [{}]" + path);
			return false;
		} catch (IOException | ImageReadException e) {
			log.error("Photoshop App13 Segment failed to remove. File path [{}]" + path);
			return false;
		}
		
		return true;
	}
	
	private void writeSegments(final OutputStream outputStream,
            final List<? extends JFIFPiece> segments) throws IOException {
        try (DataOutputStream os = new DataOutputStream(outputStream)) {
            JpegConstants.SOI.writeTo(os);

            for (final JFIFPiece piece : segments) {
                piece.write(os);
            }
        }
    }
	
	private JFIFPieces analyzeJFIF(final ByteSource byteSource) throws ImageReadException, IOException {
        final List<JFIFPiece> pieces = new ArrayList<>();
        final List<JFIFPiece> segmentPieces = new ArrayList<>();

        final JpegUtils.Visitor visitor = new JpegUtils.Visitor() {
            @Override
            public boolean beginSOS() {
                return true;
            }

            @Override
            public void visitSOS(final int marker, final byte[] markerBytes, final byte[] imageData) {
                pieces.add(new JFIFPieceImageData(markerBytes, imageData));
            }

            @Override
            public boolean visitSegment(final int marker, final byte[] markerBytes,
                    final int segmentLength, final byte[] segmentLengthBytes,
                    final byte[] segmentData) throws ImageReadException, IOException {
                final JFIFPiece piece = new JFIFPieceSegment(marker, markerBytes,
                        segmentLengthBytes, segmentData);
                pieces.add(piece);
                segmentPieces.add(piece);

                return true;
            }
        };

        new JpegUtils().traverseJFIF(byteSource, visitor);

        return new JFIFPieces(pieces, segmentPieces);
    }
	
	private static class JFIFPieces {
        public final List<JFIFPiece> pieces;
        @SuppressWarnings("unused")
		public final List<JFIFPiece> segmentPieces;

        public JFIFPieces(final List<JFIFPiece> pieces,
                final List<JFIFPiece> segmentPieces) {
            this.pieces = pieces;
            this.segmentPieces = segmentPieces;
        }

    }
	
	private interface SegmentFilter {
        boolean filter(JFIFPieceSegment segment);
    }
	
	private <T extends JFIFPiece> List<T> findPhotoshopApp13Segments(
            final List<T> segments) {
        return filterSegments(segments, PHOTOSHOP_APP13_SEGMENT_FILTER, true);
    }
	
	private <T extends JFIFPiece> List<T> filterSegments(final List<T> segments,
            final SegmentFilter filter, boolean reverse) {
        final List<T> result = new ArrayList<>();
        
        for (final T piece : segments) {
            if (piece instanceof JFIFPieceSegment) {
                if (filter.filter((JFIFPieceSegment) piece) ^ !reverse) {
                    result.add(piece);
                }
            } else if (!reverse) {
                result.add(piece);
            }
        }

        return result;
    }
	
	private static class JFIFPieceSegment extends JFIFPiece {
        public final int marker;
        private final byte[] markerBytes;
        private final byte[] segmentLengthBytes;
        private final byte[] segmentData;
 
        JFIFPieceSegment(final int marker, final byte[] markerBytes,
                final byte[] segmentLengthBytes, final byte[] segmentData) {
            this.marker = marker;
            this.markerBytes = markerBytes;
            this.segmentLengthBytes = segmentLengthBytes;
            this.segmentData = segmentData.clone();
        }

        @Override
        public String toString() {
            return "[" + this.getClass().getName() + " (0x"
                    + Integer.toHexString(marker) + ")]";
        }

        @Override
        protected void write(final OutputStream os) throws IOException {
            os.write(markerBytes);
            os.write(segmentLengthBytes);
            os.write(segmentData);
        }

        public boolean isPhotoshopApp13Segment() {
            if (marker != JpegConstants.JPEG_APP13_MARKER) {
                return false;
            }
            if (!new IptcParser().isPhotoshopJpegSegment(segmentData)) {
                return false;
            }
            return true;
        }
        
    }
	
	private class JFIFPieceImageData extends JFIFPiece {
        private final byte[] markerBytes;
        private final byte[] imageData;

        JFIFPieceImageData(final byte[] markerBytes, final byte[] imageData) {
            super();
            this.markerBytes = markerBytes;
            this.imageData = imageData;
        }

        @Override
        protected void write(final OutputStream os) throws IOException {
            os.write(markerBytes);
            os.write(imageData);
        }
    }
	
	private abstract static class JFIFPiece {
        protected abstract void write(OutputStream os) throws IOException;

        @Override
        public String toString() {
            return "[" + this.getClass().getName() + "]";
        }
    }
}
