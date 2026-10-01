<?php
 	$sFileInfo = '';
	$headers = array();
	 
	foreach($_SERVER as $k => $v) {
		if(substr($k, 0, 9) == "HTTP_FILE") {
			$k = substr(strtolower($k), 5);
			$headers[$k] = $v;
		} 
	}

    $name_arr = explode(".", rawurldecode($headers['file_name']));
    $name = str_replace("\0", "", time().'-'.rand(0,100).'.'.$name_arr[1]);
    $file = new stdClass;
    $file->name = str_replace("\0", "", rawurldecode($name));
    $file->size = $headers['file_size'];
    $file->content = file_get_contents("php://input");
	
	$filename_ext = strtolower(array_pop(explode('.',$file->name)));
	$allow_file = array("jpg", "png", "bmp", "gif"); 

	if(!in_array($filename_ext, $allow_file)) {
		echo "NOTALLOW_".$file->name;
	} else {
		$uploadDir = $_SERVER['DOCUMENT_ROOT'].'/smart_editor/upload/';
		if(!is_dir($uploadDir)){
			mkdir($uploadDir, 0777);
		}
		
		//$newPath = $uploadDir.iconv("utf-8", "cp949", $file->name);
        $newPath = $_SERVER['DOCUMENT_ROOT'].'/smart_editor/upload/'.iconv("utf-8", "cp949", $file->name);
		
		if(file_put_contents($newPath, $file->content)) {
			$sFileInfo .= "&bNewLine=true";
			$sFileInfo .= "&sFileName=".$file->name;
			$sFileInfo .= "&sFileURL=/smart_editor/upload/".$file->name;
		}
		
		echo $sFileInfo;
	}
?>