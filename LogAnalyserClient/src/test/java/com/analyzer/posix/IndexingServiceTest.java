package com.analyzer.posix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.analyzer.commons.CustomConcurrentMap;
import com.analyzer.commons.FileIndexModel;

public class IndexingServiceTest {
	@Test
	public void indexingService() {
		String dateFormat = "DDDDDDDD";
		final CustomConcurrentMap filePathMap = new CustomConcurrentMap();
		for (int i = 0; i < (Math.random() + 1) * 10; i++) {
			List<FileIndexModel> indexes = new ArrayList<FileIndexModel>();
			for (int j = 0; j < (Math.random() + 1) * 10; j++) {
				FileIndexModel f = new FileIndexModel(String.valueOf(j));
				f.setDateFormat(dateFormat);
				indexes.add(f);
			}
			filePathMap.put(String.valueOf(i), indexes);
		}

		CustomConcurrentMap newFilePathMap = new CustomConcurrentMap();
		for (int i = 0; i < (Math.random() + 1) * 10; i++) {
			List<FileIndexModel> indexes = new ArrayList<FileIndexModel>();
			for (int j = 0; j < (Math.random() + 1) * 10; j++) {
				indexes.add(new FileIndexModel(String.valueOf(j)));
			}
			newFilePathMap.put(String.valueOf(i), indexes);
		}

		
		int priorMatch =0;
		for (Map.Entry<String, List<FileIndexModel>> indices : filePathMap.entrySet()) {
			if(newFilePathMap.get(indices.getKey()) != null) {
				List<FileIndexModel> newList = newFilePathMap.get(indices.getKey());
				List<FileIndexModel> oldList = indices.getValue();
				for(FileIndexModel oIndex: oldList) {
					for(FileIndexModel nIndex: newList) {
						if (oIndex.getFileName().equals(nIndex.getFileName())) {
							priorMatch++;
							continue;
						}
					}
				}
			}
		}
		filePathMap.setNewmap(newFilePathMap);
		int afterMatch=0;
		for(Map.Entry<String, List<FileIndexModel>> indices : filePathMap.entrySet()) {
			for(FileIndexModel index : indices.getValue()) {
				if(dateFormat.equals(index.getDateFormat())) {
					afterMatch++;
				}
			}
		}
		
		Assert.assertEquals(priorMatch, afterMatch);
	}
}
