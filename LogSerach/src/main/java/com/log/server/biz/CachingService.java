package com.log.server.biz;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.log.server.LocalConstants;
import com.log.server.SpringHelper;
import com.log.server.model.LogRecordModel;
import com.log.server.model.ScrollableResultModel;
import com.log.server.model.SearchInput;
import com.log.server.model.ViewResultModel;

public class CachingService {
	/**
	 * 
	 * @param input
	 * @return
	 * @throws Exception 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public ScrollableResultModel getNextScroll(SearchInput input) throws InterruptedException, ExecutionException, Exception{
		
		ViewResultModel result= SpringHelper.searchBean().getSearchResult(input,input.getKey());
		
		ScrollableResultModel scroll = new ScrollableResultModel();
		List<LogRecordModel> records = null;
		
		int startIndex = input.getPaginationIndex();
		int endIndex = input.getPageLength() + startIndex;
		if ("dated".equalsIgnoreCase(input.getScrollType())){
			if(startIndex > result.getDatedList().size()){
				return scroll;
			}
			if(endIndex > result.getDatedList().size()){
				records = result.getDatedList().subList(startIndex, result.getDatedList().size());
				scroll.setScrollAtIndex(result.getDatedList().size());
			}else{
				records =  result.getDatedList().subList(startIndex, endIndex);
				scroll.setScrollAtIndex(endIndex);
			}
			scroll.setData(records);
			return scroll;
		}
		if("undated".equalsIgnoreCase(input.getScrollType())){
			if(startIndex > result.getUnDatedList().size()){
				return scroll;
			}
			if(endIndex > result.getUnDatedList().size()){
				records = result.getUnDatedList().subList(startIndex, result.getUnDatedList().size());
				scroll.setScrollAtIndex(result.getUnDatedList().size());
			}else{
				records = result.getUnDatedList().subList(startIndex, endIndex);
				scroll.setScrollAtIndex(endIndex);
			}
			scroll.setData(records);
			return scroll;
		}
		return scroll;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws Exception
	 */
	public ViewResultModel getPaginatedSearchResult(SearchInput input) throws InterruptedException, ExecutionException, Exception{
		
		ViewResultModel model= SpringHelper.searchBean().getSearchResult(input, input.getKey());
		ViewResultModel paginatedModel = new ViewResultModel();
		paginatedModel.setAllowedTimeZoneList(model.getAllowedTimeZoneList());
		paginatedModel.setDatedList(model.getDatedList());
		
		int datedSize = model.getDatedList()==null?0:model.getDatedList().size();
		
		if(datedSize>LocalConstants.DEFAULT_PAGE_LENGTH+(LocalConstants.DEFAULT_PAGE_LENGTH/10)){
			paginatedModel.setDatedList(model.getDatedList().subList(0, LocalConstants.DEFAULT_PAGE_LENGTH));
		}else{
			paginatedModel.setDatedList(model.getDatedList());
		}		
		
		paginatedModel.setGraphicsData(model.getGraphicsData());
		paginatedModel.setExplicitErrorMessage(model.getExplicitErrorMessage());
		paginatedModel.setFaultNodeMessages(model.getFaultNodeMessages());
		paginatedModel.setFilesWithMatchCount(model.getFilesWithMatchCount());
		paginatedModel.setInput(model.getInput());
		paginatedModel.setLinesFetched(model.getLinesFetched());
		paginatedModel.setMadatorySearchKeyword(model.getMadatorySearchKeyword());
		paginatedModel.setMaxFileLimitBreached(model.isMaxFileLimitBreached());
		paginatedModel.setNodeMessages(model.getNodeMessages());
		paginatedModel.setSearchedFilesCount(model.getSearchedFilesCount());
		paginatedModel.setSearchKeyword(model.getSearchKeyword());
		paginatedModel.setServersSearched(model.getServersSearched());
		
		int nonDatedSize = model.getUnDatedList()==null?0:model.getUnDatedList().size();
		
		if(nonDatedSize>LocalConstants.DEFAULT_PAGE_LENGTH+(LocalConstants.DEFAULT_PAGE_LENGTH/10)){
			paginatedModel.setUnDatedList(model.getUnDatedList().subList(0, LocalConstants.DEFAULT_PAGE_LENGTH));
		}else{
			paginatedModel.setUnDatedList(model.getUnDatedList());
		}		
		paginatedModel.setUnDatedTextLines(model.getUnDatedTextLines());
		
		return paginatedModel;
	}
}
