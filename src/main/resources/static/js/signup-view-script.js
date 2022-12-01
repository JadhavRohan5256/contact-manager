import { FileDetails } from './helper.js'
import { HideImage } from './helper.js'
const fileHTML = document.querySelector('#file');
const selectedFileHTML = document.querySelector('.selected-file');
const fileSizeHTML = document.querySelector('.file-size');
const selectedFileViewHTML = document.querySelector('.selected-file-view');

if (fileHTML != null) {
	fileHTML.addEventListener('change', (e) => {
		FileDetails(fileSizeHTML, selectedFileHTML, selectedFileViewHTML, e);
	})
}

HideImage(selectedFileViewHTML);