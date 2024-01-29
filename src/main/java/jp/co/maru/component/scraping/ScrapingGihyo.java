package jp.co.maru.component.scraping;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.stereotype.Component;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@Component
public class ScrapingGihyo {
	
	private final Path pyFilePath = Paths.get("").toAbsolutePath().resolve(Paths.get("fetch_data.py"));
	private Path dataFilePath;
	private Path imageDirPath;
	private Path venvPath;
	
	private List<List<String>> gihyo_data;
	
	public ScrapingGihyo() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		//json取得
		JsonObject jsonObject = JsonParser.parseReader(new FileReader("config.json")).getAsJsonObject();
		dataFilePath = Paths.get("").toAbsolutePath().resolve(jsonObject.get("data_file_path").getAsString());
		imageDirPath = Paths.get("").toAbsolutePath().resolve(jsonObject.get("image_dir_path_from_java").getAsString());	
		venvPath = Paths.get("").toAbsolutePath().resolve(jsonObject.get("venv_path").getAsString());
		delete();
	}
	
	public boolean fetchData(){
		try {			       
			/*python実行*/
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd", "/c", venvPath.toString() + " && python " + pyFilePath.toString());
			Process process = processBuilder.start();
			
			/*エラーがあったらreturn*/
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			@SuppressWarnings("unused")
			String errorLine;
			if((errorLine = errorReader.readLine()) != null) {
				System.out.println("execute python failed");
				return false;
			}
				
			/*データの受け取り*/
			Optional<List<List<String>>> rtn = readFile();
			if(rtn.isEmpty()) {
				System.out.println("data_file can't read");
				return false;
			}else {
				gihyo_data = rtn.get();
			}
			
			
		}catch(Exception e) {
			System.out.println("file io error");
			return false;
		}
		return true;
		
	}
	
	public List<List<String>> getBooksData(){
		return gihyo_data;
	}
	
	public boolean delete(){
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
			System.out.println("file delete error");
			return false;
		}

	}
	
	private Optional<List<List<String>>> readFile() throws FileNotFoundException{
		if(!dataFilePath.toFile().canRead()) {
			return Optional.empty();
		}
		
		List<List<String>> rtn = new ArrayList<>();
		Scanner scanner = new Scanner(new FileInputStream(dataFilePath.toString()));
		scanner.useDelimiter(",|\n");
		try(scanner){
			while(scanner.hasNext()) {
				List<String> tmpLine = new ArrayList<>();
				tmpLine.add(scanner.next());
				tmpLine.add(scanner.next());
				tmpLine.add(scanner.next());
				rtn.add(tmpLine);
			}
		}
		return Optional.of(rtn);
	}
}
