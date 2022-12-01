export const ConvertSize = (fileSize) => {
	if (fileSize < 1024) {
		fileSize += ' byte';
	}
	else if (Math.ceil(fileSize / 1024) < 1024) {
		fileSize = (fileSize / 1024).toFixed(2) + ' KB';
	}
	else if (Math.ceil((fileSize / 1024) / 1024) < 1024) {
		fileSize = ((fileSize / 1024) / 1024).toFixed(2) + ' MB';
	}
	else {
		fileSize = (((fileSize / 1024) / 1024) / 1024).toFixed(2) + ' GB';
	}
	return fileSize;
}


export function FileDetails(fileSizeHTML, selectedFileHTML, selectedFileViewHTML, event) {
	const width = window.innerWidth;
	let fileObject = event.target.files[0];
	let fileName = fileObject.name;
	let fileSize = fileObject.size;
	let extension = fileName.split(".");
	let charSize = width < 600 ? 7 : 20;
	extension = extension[extension.length - 1];
	//coverting short file name 
	fileName = fileName.length > charSize
				? fileName.substr(0, charSize) + '..' + extension
				: fileName;
	fileSize = ConvertSize(fileSize);
	fileSizeHTML.textContent = fileSize;
	selectedFileHTML.textContent = fileName;
	selectedFileViewHTML.src = URL.createObjectURL(fileObject);

}

export function HideImage(selectedFileView) {
	if(selectedFileView != null) selectedFileView.addEventListener('dblclick', () => selectedFileView.src="");
}
