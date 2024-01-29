package jp.co.maru.component.scraping;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/*
 * スクレイピング用クラス
 */
@Component
public class Scraping {
	private final Path pyFilePath = Paths.get("").toAbsolutePath().resolve(Paths.get("fetch_data.py"));
	private Path dataFilePath;
	private Path imageDirPath;
	private Path venvPath;
	
	/*
	 * コンストラクタ
	 * jsonからpathを受け取ってフィールドに入れておく
	 */
	public Scraping() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		//json取得
		JsonObject jsonObject = JsonParser.parseReader(new FileReader("config.json")).getAsJsonObject();
		dataFilePath = Paths.get("").toAbsolutePath().resolve(jsonObject.get("data_file_path").getAsString());
		imageDirPath = Paths.get("").toAbsolutePath().resolve(jsonObject.get("image_dir_path_from_python").getAsString());	
		venvPath = Paths.get("").toAbsolutePath().resolve(jsonObject.get("venv_path").getAsString());
	}
	 
	/*
	 * pythonを実行してスクレイピング
	 */
	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Tokyo")
	public void fetchData(){
		//古いデータの削除
		if(!delete()) {
			System.out.println("既存ファイルの削除中にエラーが発生しました。");
		}
		
		try {			       
			/*python実行*/
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd", "/c", venvPath.toString() + " && python " + pyFilePath.toString());
			Process process = processBuilder.start();
			
			//process中でエラーが発生したらthrow
			int exitCode = process.waitFor();
			if(exitCode != 0) {
				throw new Exception();
			}  
			
		}catch(Exception e) {
			System.out.println("execute python failed");
		}		
	}
	
	/*
	 * データを格納したファイルへのパス
	 */
	public Path getDataFilePath() {
		return dataFilePath;
	}
	
	/*
	 * 既存ファイルの消去
	 */
	private boolean delete(){
		try {
			if (Files.exists(imageDirPath)) {
				Files.walkFileTree(imageDirPath, new SimpleFileVisitor<Path>(){
					@Override
					public FileVisitResult visitFile(
							Path file,
							BasicFileAttributes attrs
							)throws IOException{
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}
				});
			}
			if(Files.exists(dataFilePath)) {
				Files.delete(dataFilePath);
			}			
			return true;
		}catch(IOException e) {
			return false;
		}
	}
}
