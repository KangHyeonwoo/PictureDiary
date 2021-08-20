/**
 *	Toast Message Class
 *	
 */
//참고 블로그 : https://marshall-ku.tistory.com/307
export default class Toast {
	
	static show(message) {
		const toast = document.getElementById('toast');
		
		let removeToast;
		
		toast.classList.contains('reveal') 
			? (clearTimeout(removeToast), removeToast = setTimeout(() => toast.classList.remove('reveal'), 1000))
			: removeToast = setTimeout(() => toast.classList.remove('reveal'), 1000);
		
		toast.classList.add('reveal');
		toast.innerText = message;
	}
}