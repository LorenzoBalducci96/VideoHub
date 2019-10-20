//graphic related scripts...
function openUploadVideoForm() {
	document.getElementById("uploadVideoForm").style.display = "block";
}

function openCreateFolderForm(){
	document.getElementById("createFolderForm").style.display = "block";
}

function closeForm() {
	document.getElementById("uploadVideoForm").style.display = "none";
}

function closeFormCreateFolder() {
	document.getElementById("createFolderForm").style.display = "none";
}

/*	NOT WORKING FIREFOX SCROLLBAR STYLE
$(document).ready(function(){
    $('.scrollbar-inner').scrollbar();
});
 */
//end of graphic related scripts...


//business logic scripts	
var backButton = document.getElementById("back");
var actualFolder = "";
var actualFolderShower = document.getElementById("actualFolder");

var video = document.getElementById('videoPlayer');

window.onload = function() {
	backButton.style.visibility = "hidden";
	actualFolder = "";
	actualFolderShower.innerHTML = "";  
	requestData("");
};

function requestData(requested_folder) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			actualFolder = requested_folder;
			setData(this.responseText);
			if(requested_folder.trim() != ""){
				backButton.style.visibility = "visible";
				backButton.id = requested_folder.substr(0,requested_folder.lastIndexOf("/"));
				backButton.onclick = function() {
					requestData(this.id);
				};
				actualFolderShower.innerHTML = requested_folder;//.substr(0,requested_folder.lastIndexOf("/"));
			}
			else{
				backButton.style.visibility = "hidden";
				actualFolderShower.innerHTML = requested_folder;
			}
		}
	};
	xhttp.open("POST", "requestData?requested_folder=" + requested_folder, true);
	xhttp.send();
}


//upload handling
function uploadVideoAjax() {
	var file = document.getElementById("video").files[0];
	var path = actualFolder;
	// alert(file.name+" | "+file.size+" | "+file.type);
	var formdata = new FormData();
	//formdata.append("path", "/new/try");
	formdata.append("path", path);
	formdata.append("video", file);
	var ajax = new XMLHttpRequest();
	ajax.upload.addEventListener("progress", progressHandler, false);
	ajax.addEventListener("load", completeHandler, false);
	ajax.addEventListener("error", errorHandler, false);
	ajax.addEventListener("abort", abortHandler, false);
	ajax.open("POST", "uploadData"); // http://www.developphp.com/video/JavaScript/File-Upload-Progress-Bar-Meter-Tutorial-Ajax-PHP
	//use file_upload_parser.php from above url
	ajax.send(formdata);
}

function progressHandler(event) {
	document.getElementById("loaded_n_total").innerHTML = "Uploaded " + event.loaded + " bytes of " + event.total;
	var percent = (event.loaded / event.total) * 100;
	document.getElementById("progressBar").value = Math.round(percent);
	document.getElementById("status").innerHTML = Math.round(percent) + "% uploaded... please wait";
}

function completeHandler(event) {
	document.getElementById("status").innerHTML = event.target.responseText;
	document.getElementById("progressBar").value = 0; //wil clear progress bar after successful upload
	requestData(actualFolder);
}

function errorHandler(event) {
	document.getElementById("status").innerHTML = "Upload Failed";
}

function abortHandler(event) {
	document.getElementById("status").innerHTML = "Upload Aborted";
}
//end of handling update file


function createFolderAjax() {
	var folderPath = actualFolder + "/" + document.getElementById('folderPath').value;
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			closeFormCreateFolder();
			requestData(actualFolder);
		}
	};
	xhttp.open("POST", "createFolder?folderPath=" + folderPath, true);
	xhttp.send();
}

function openPlayer(path){
	path = "SERVER_DATA" + path;

	video.pause();
	video.setAttribute('src', path);
	video.load();
	//video.play();
	video.play().catch((e) => {console.log("error: not deployed folder")});
}

function setData(text){
	var table = document.getElementById("myTable");
	table.innerHTML = "";
	var yourLines = text.split("\r\n");
	var i = 0;
	while(i < yourLines.length){
		if(yourLines[i].trim() == "file" || yourLines[i].trim() == "folder"){
			var row = table.insertRow(0);
			row.className = "rounded-grey-table"

				var cell1 = row.insertCell(0);
			var cell2 = row.insertCell(1);




			if(yourLines[i].trim() == "folder"){
				
				//cell1.innerHTML = '<img src="folder.png" width="100%" height="100%" />';
				cell1.innerHTML = '<img src="folder.png" class="icon">'; //width="' + (window.screen.height * window.devicePixelRatio) / 20 + '" height="' + (window.screen.height * window.devicePixelRatio) / 20 + '" />';
				row.id = yourLines[i + 1];
				row.onclick = function() { requestData( actualFolder + "/" + this.id) }
			}
			if(yourLines[i].trim() == "file"){
				var screenHeigth = screen.height;
				//alert("screen heigth:" + screenHeigth)
				cell1.innerHTML = '<img src="video.png" class="icon">'; // width="' + (window.screen.height * window.devicePixelRatio) / 20 + 'em" height="' + (window.screen.height * window.devicePixelRatio) / 20 + 'em" />';
				//cell1.innerHTML = '<img src="video.png" width="20" height="20" />';
				row.id = actualFolder + "/" + yourLines[i + 1];
				row.onclick = function() { openPlayer(this.id) }
			}
			cell2.innerHTML = yourLines[i + 1];
			yourLines[i + 1];

			//cell2.onclick = function() {
			//	  requestData("/" + cells[2].nodeValue);
			//};

		}else if(yourLines[i].trim() == "free_space"){
			var freeSpace = yourLines[i + 1].trim();
			freeSpace = freeSpace / 1024; //kb
			freeSpace = freeSpace / 1024; //mb
			if(freeSpace > 1024){
				freeSpace = freeSpace / 1024;
				freeSpace = Math.round( freeSpace * 10) / 10;
				document.getElementById("freeSpace").innerHTML = freeSpace + " GB"; 
			}
			else{
				freeSpace = Math.round( freeSpace * 10) / 10;
				document.getElementById("freeSpace").innerHTML = freeSpace + " MB"; 
			}
		}
		i = i + 2;
	}
}

//fill invisible form with path
function fillPathValue() {
	document.getElementById('file_path_in_upload_form').value = actualFolder;
	return true;
}

/*
function fillCreateFolderPathValue(){
	document.getElementById('folderPath').value = actualFolder + "/" + document.getElementById('folderPath').value;
	return true;
}
 */