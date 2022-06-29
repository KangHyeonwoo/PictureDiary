package com.picture.diary.utils;

import com.picture.diary.common.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public class NasConnectionErrorResponse {
    private Error error;
    private boolean success;

    @Getter
    public class Error {
        private int code;
    }

    /**
     * NasConnectionErrorResponse 객체를  ErrorResponse 객체로 만들어 리턴
     * @return
     */
    public ErrorResponse toErrorResponse() {
        int code = this.error.code;
        String message = ErrorResponseType.findKorMessageBy(this.error.code);

        return new ErrorResponse(code, message);
    }

    //Synology API docs 에 정의된 예외 정리
    //참조 : https://global.download.synology.com/download/Document/Software/DeveloperGuide/Package/FileStation/All/enu/Synology_File_Station_API_Guide.pdf?_ga=2.236408261.592286122.1650902771-2131878755.1649515883
    @AllArgsConstructor
    private enum ErrorResponseType {
        _100(100, "Unknown error", "알 수 없는 에러"),
        _101(101, "No Parameter of API, method or version", "API, 메서드 혹은 버전에 맞지 않은 파라미터"),
        _102(102, "The requested API does not exist", "존재하지 않는 API 요청"),
        _103(103, "The requested method does not exist", "존재하지 않는 메서드"),
        _104(104, "The requested version does not support the functionality", "해당 버전에서는 지원하지 않는 메서드"),
        _105(105, "The logged in session does not have permission", "권한 없음"),
        _106(106, "Session timeout", "세션 타임아웃"),
        _107(107, "Session interrupted by duplicate login", "중복 로그인으로 인한 세션 충돌"),
        _119(119, "SID not found", "SID 를 찾을 수 없음"),

        //file system
        _400(400, "Invalid parameter of file operation", "잘못된 파라미터 요청"),
        _401(401, "Unknown error of file operation", "기타 에러"),
        _402(402, "System is too busy", "시스템 요청 과부하"),
        _403(403, "Invalid user does this file operation", "잘못된 사용자"),
        _404(404, "Invalid group does this file operation", "잘못된 그룹"),
        _405(405, "Invalid user and group does this file operation", "잘못된 사용자 혹은 그룹"),
        _406(406, "Can't get user/group information from the account server", "사용자 혹은 그룹의 정보를 조회할 수 없음"),
        _407(407, "Operation not permitted", "권한 없음"),
        _408(408, "No such file or directory", "파일 혹은 경로가 없음"),
        _409(409, "Non-supported file system", "지원하지 않음"),
        _410(410, "Failed to connect internet-based file system(e.g., CIFS", "인터넷 연결 실패"),
        _411(411, "Read-only file system", "읽기만 가능"),
        _412(412, "Filename too long in the non-encrypted file system", "암호화되지 않은 파일 이름이 너무 긺"),
        _413(413, "Filename too long in the encrypted file system", "암호화된 파일 이름이 너무 긺"),
        _414(414, "File already exists", "이미 존재하는 파일"),
        _415(415, "Disk quota exceed" , "디스크 할당량 초과"),
        _416(416, "No space left on device", "남는 작업공간이 없음"),
        _417(417, "Input/output error", "IO 예외"),
        _418(418, "Illegal name or path", "잘못된 경로 혹은 명칭"),
        _419(419, "Illegal file name", "잘못된 파일명"),
        _420(420, "Illegal file name on FAT file system", "FAT 파일 시스템의 잘못된 파일명"),
        _421(421, "Device or resource busy", "요청량 많음"),
        _599(599, "No such task of the file opertaion.", "작업을 찾을 수 없음");

        private int code;
        private String engMessage;
        private String korMessage;

        static String findKorMessageBy(int code) {
            return Stream.of(ErrorResponseType.values())
                    .filter(errorResponseType -> errorResponseType.code == code)
                    .findFirst()
                    .orElse(ErrorResponseType._401)
                    .korMessage;
        }
    }
}
