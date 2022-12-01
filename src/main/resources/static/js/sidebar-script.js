let sidebar = document.querySelector('.sidebar');
let sidebarCheck = document.querySelector('#sidebar-check');
let sidebarMenuIcon = document.querySelector('.sidebar-menu-icon');
let sidebarMenu = document.querySelector('.sidebar-menu');

if (sidebarMenuIcon != null) {
	sidebarMenuIcon.addEventListener('click', () => {
		if (sidebarCheck.checked == true) {
			sidebar.style.transform = "translateX(-202px)";
			sidebarMenu.style.transform = "translate(0, 0)";
		}
		else {
			sidebar.style.transform = "translateX(0px)";
			sidebarMenu.style.transform = "translateX(154px) translateY(42px)";

		}
	});
}



