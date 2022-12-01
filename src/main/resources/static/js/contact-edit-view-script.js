import { FileDetails } from './helper.js'
import { HideImage } from './helper.js'
const editFileHTML = document.querySelector('#editFile');
const editSelectedFileHTML = document.querySelector('.edit-selected-file');
const editFileSizeHTML = document.querySelector('.edit-file-size');
const editSelectedFileViewHTML = document.querySelector('.edit-selected-file-view');

if (editFileHTML != null) {
	editFileHTML.addEventListener('change', (e) => {
		FileDetails(editFileSizeHTML, editSelectedFileHTML, editSelectedFileViewHTML, e);
	})
}


HideImage(editSelectedFileViewHTML);