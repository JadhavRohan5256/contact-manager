const themeFlag = document.querySelector('#themeFlag');
const themeMode = document.querySelector('.theme-mode');
let root = document.documentElement;

const dark = () => {
	root.style.setProperty('--bg-color', '#000000');
	root.style.setProperty('--bg-surface-color', '#121212');
	root.style.setProperty('--font-color', '#808080');
	root.style.setProperty('--primary-color', '#81D4FA');
	root.style.setProperty('--secondary-color', '#FF5722');
	root.style.setProperty('--error-color', '#721c24');
	root.style.setProperty('--error-bg', '#f8d7da');
	root.style.setProperty('--success-color', '#155724');
	root.style.setProperty('--success-bg', '#d4edda');
	root.style.setProperty('--success-border-color', '#c3e7cb');
	root.style.setProperty('--transparent-color', 'transparent');
}

const light = () => {
	root.style.setProperty('--bg-color', '#eee');
	root.style.setProperty('--bg-surface-color', '#ffffff');
	root.style.setProperty('--font-color', '#121212');
	root.style.setProperty('--primary-color', '#81D4FA');
	root.style.setProperty('--secondary-color', '#FF5722');
	root.style.setProperty('--error-color', '#721c24');
	root.style.setProperty('--error-bg', '#f8d7da');
	root.style.setProperty('--success-color', '#155724');
	root.style.setProperty('--success-bg', '#d4edda');
	root.style.setProperty('--success-border-color', '#c3e7cb');
	root.style.setProperty('--transparent-color', 'transparent');
}


const applyTheme = () => {
	let darkMode = localStorage.getItem("darkMode");
	if (darkMode === null || darkMode === 'true') {
		dark();
		themeMode.innerHTML = '<i class="fa-solid fa-moon theme-icons"></i>';		
		themeFlag.checked = false;
		localStorage.setItem("darkMode", true);
	} else {
		light();
		themeMode.innerHTML = '<i class="fa-solid fa-sun theme-icons"></i>';
		themeFlag.checked = true;
	}
}

applyTheme();
window.addEventListener('load', applyTheme())

themeFlag.addEventListener('change', () => {
	if (themeFlag.checked == true) {
		themeMode.innerHTML = '<i class="fa-solid fa-sun theme-icons"></i>';
		localStorage.setItem("darkMode", false);
		light();
	}
	else {
		themeMode.innerHTML = '<i class="fa-solid fa-moon theme-icons"></i>';
		localStorage.setItem("darkMode", true);
		dark();
	}
})


//tost message 
let toast = document.querySelector('.toast');
let toastClose = document.querySelector('.close-toast');

if (toastClose != null) {
	toastClose.addEventListener('click', () => {
		toast.style.display = "none";
	});
}