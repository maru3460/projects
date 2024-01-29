package jp.co.maru.component.scraping;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/*
 * スクレイピングして保存したデータを扱うクラス
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ManageScraping {	
	private List<List<String>> gihyoData;
	private Path dataFilePath;
	
	public ManageScraping(Scraping scraping) {
		this.dataFilePath = scraping.getDataFilePath();
	}
	
	public List<List<String>> getData(){
		return gihyoData;
	}
	
	/*
	 * pathのファイルからデータを受け取ってフィールドに格納
	 */
	public boolean setData() {
		Optional<List<List<String>>> rtn = readFile();
		if(rtn.isEmpty()) {
			System.out.println("data_file can't read");
			return false;
		}
		
		gihyoData = rtn.get();
		return true;
	}
	
	/*
	 * データの読み取り
	 */
	private Optional<List<List<String>>> readFile(){
		if(!dataFilePath.toFile().canRead()) {
			return Optional.empty();
		}
		
		List<List<String>> rtn = new ArrayList<>();
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(dataFilePath.toString()));		
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
		}catch(Exception e) {
			System.out.println("file io error");
		}
		return Optional.of(rtn);
	}
}
