package com.nnk.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BidTests {

	@Mock
	private BidListRepository bidListRepository;

	@InjectMocks
	private BidListService bidListService;
	
	@Test
	public void testGetAllBidList_WhenSuccess() {
		BidList bid = new BidList();
		bid.setId(1);
		bid.setAccount("Account1");
		bid.setBidQuantity(10d);
		
		List<BidList> expectedBidList = new ArrayList();
		
		expectedBidList.add(bid);
		
		when(bidListRepository.findAll()).thenReturn(expectedBidList);
		
		List<BidList> actualBidList = bidListService.getAllBidList();
		
		assertEquals(expectedBidList.size(), actualBidList.size());
		assertEquals(expectedBidList, actualBidList);
	}

	@Test
	public void testGetAllBidList_WhenException() {
		List<BidList> bidList = new ArrayList<>();
		
		when(bidListRepository.findAll()).thenReturn(bidList);
		
		List<BidList> actualBidList = bidListService.getAllBidList();
		
		assertEquals(0, actualBidList.size());
		assertEquals(bidList, actualBidList);
	}
	

}
