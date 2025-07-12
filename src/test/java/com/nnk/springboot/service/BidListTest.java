package com.nnk.springboot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;

@ExtendWith(MockitoExtension.class)
public class BidListTest {

	@Mock
	private BidListRepository bidListRepository;

	@InjectMocks
	private BidListService bidListService;

	private BidList bid;

	@BeforeEach()
	void setUp() {
		bid = new BidList();
		bid.setId(1);
		bid.setAccount("Account1");
		bid.setBidQuantity(10d);
	}

	@Test
	public void testGetAllBidList_WhenSuccess() {

		List<BidList> expectedBidList = new ArrayList<>();

		expectedBidList.add(bid);

		when(bidListRepository.findAll()).thenReturn(expectedBidList);

		List<BidList> actualBidList = bidListService.getAllBidList();

		assertEquals(expectedBidList.size(), actualBidList.size());
		assertEquals(expectedBidList, actualBidList);
	}

	@Test
	public void testSaveBidList_WhenSuccess() {

		when(bidListRepository.save(any(BidList.class))).thenReturn(bid);

		BidList savedBid = bidListService.saveBidList(bid);

		assertEquals(bid.getAccount(), savedBid.getAccount());
		assertEquals(bid.getBidQuantity(), savedBid.getBidQuantity());
		verify(bidListRepository, times(1)).save(any(BidList.class));
	}

	@Test
	public void testSaveBidList_WhenException() {

		doThrow(new RuntimeException("Erreur lors de la sauvegarde")).when(bidListRepository).save(any(BidList.class));

		assertThrows(RuntimeException.class, () -> {
			bidListService.saveBidList(bid);
		});
	}

	@Test
	public void testGetBidListById_WhenSuccess() {

		when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(bid));

		BidList foundBid = bidListService.getBidListById(1);

		assertEquals(1, foundBid.getId());
		assertEquals("Account1", foundBid.getAccount());
		assertEquals(10d, foundBid.getBidQuantity());

		verify(bidListRepository, times(1)).findById(anyInt());
	}

	@Test
	public void testGetBidListById_WhenException() {
		doThrow(new RuntimeException("Erreur lors de la récupération du bid")).when(bidListRepository)
				.findById(anyInt());

		assertThrows(RuntimeException.class, () -> {
			bidListService.getBidListById(1);
		});
		verify(bidListRepository, times(1)).findById(anyInt());

	}

	@Test
	public void testUpdateBidListById_WhenSuccess() {
		when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(bid));

		BidList newBidData = new BidList();
		newBidData.setAccount("accountUpdated");
		newBidData.setBidQuantity(20.0);

		BidList expectedSavedBid = new BidList();
		expectedSavedBid.setId(bid.getId());
		expectedSavedBid.setAccount(newBidData.getAccount());
		expectedSavedBid.setBidQuantity(newBidData.getBidQuantity());

		when(bidListRepository.save(any(BidList.class))).thenReturn(expectedSavedBid);

		BidList actualUpdatedBid = bidListService.updateBidListById(bid.getId(), newBidData);

		assertEquals(expectedSavedBid.getAccount(), actualUpdatedBid.getAccount());
		assertEquals(expectedSavedBid.getBidQuantity(), actualUpdatedBid.getBidQuantity());
		assertEquals(expectedSavedBid.getId(), actualUpdatedBid.getId());

		verify(bidListRepository, times(1)).findById(bid.getId());
		verify(bidListRepository, times(1)).save(any(BidList.class));
	}

	@Test
	public void testUpdateBidListById_WhenExceptionDuringSave() {
		when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(bid));

		BidList newBidData = new BidList();
		newBidData.setAccount("test");
		newBidData.setBidQuantity(100.0);

		doThrow(new RuntimeException("Erreur lors de la sauvegarde de la mise à jour")).when(bidListRepository)
				.save(any(BidList.class));

		assertThrows(RuntimeException.class, () -> {
			bidListService.updateBidListById(bid.getId(), newBidData);
		});
		verify(bidListRepository, times(1)).findById(anyInt());
		verify(bidListRepository, times(1)).save(any(BidList.class));
	}

	@Test
	public void testUpdateBidListById_WhenNotFound() {
		when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());

		BidList newBidData = new BidList();
		newBidData.setAccount("AccountNonExistant");
		newBidData.setBidQuantity(50.0);

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			bidListService.updateBidListById(1, newBidData);
		});

		assertEquals("Aucun bid trouvé", thrown.getMessage());

		verify(bidListRepository, times(1)).findById(anyInt());
		verify(bidListRepository, never()).save(any(BidList.class));
	}

	@Test
	public void testDeleteBidListById_WhenSuccess() {
		when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(bid));

		bidListService.deleteById(bid.getId());

		verify(bidListRepository, times(1)).findById(anyInt());
		verify(bidListRepository, times(1)).delete(bid);
	}

	@Test
	public void testDeleteBidListById_WhenNotFound() {
		when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			bidListService.deleteById(999);
		});

		verify(bidListRepository, times(1)).findById(anyInt());
		verify(bidListRepository, never()).deleteById(anyInt());
	}
	
	   @Test
	    public void testBidListGettersAndSetters() {
	        BidList bidList = new BidList();

	        bidList.setId(1);
	        bidList.setAccount("Account Test");
	        bidList.setType("Type Test");
	        bidList.setBidQuantity(10.0);
	        bidList.setAskQuantity(20.0);
	        bidList.setBid(15.0);
	        bidList.setAsk(25.0);
	        bidList.setBenchmark("Benchmark Test");
	        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
	        bidList.setBidListDate(now);
	        bidList.setCommentary("Commentary Test");
	        bidList.setSecurity("Security Test");
	        bidList.setStatus("Status Test");
	        bidList.setTrader("Trader Test");
	        bidList.setBook("Book Test");
	        bidList.setCreationName("Creation Name Test");
	        bidList.setCreationDate(now);
	        bidList.setRevisionName("Revision Name Test");
	        bidList.setRevisionDate(now);
	        bidList.setDealName("Deal Name Test");
	        bidList.setDealType("Deal Type Test");
	        bidList.setSourceListId("Source List ID Test");
	        bidList.setSide("Side Test");

	        assertEquals(1, bidList.getId());
	        assertEquals("Account Test", bidList.getAccount());
	        assertEquals("Type Test", bidList.getType());
	        assertEquals(10.0, bidList.getBidQuantity());
	        assertEquals(20.0, bidList.getAskQuantity());
	        assertEquals(15.0, bidList.getBid());
	        assertEquals(25.0, bidList.getAsk());
	        assertEquals("Benchmark Test", bidList.getBenchmark());
	        assertEquals(now, bidList.getBidListDate());
	        assertEquals("Commentary Test", bidList.getCommentary());
	        assertEquals("Security Test", bidList.getSecurity());
	        assertEquals("Status Test", bidList.getStatus());
	        assertEquals("Trader Test", bidList.getTrader());
	        assertEquals("Book Test", bidList.getBook());
	        assertEquals("Creation Name Test", bidList.getCreationName());
	        assertEquals(now, bidList.getCreationDate());
	        assertEquals("Revision Name Test", bidList.getRevisionName());
	        assertEquals(now, bidList.getRevisionDate());
	        assertEquals("Deal Name Test", bidList.getDealName());
	        assertEquals("Deal Type Test", bidList.getDealType());
	        assertEquals("Source List ID Test", bidList.getSourceListId());
	        assertEquals("Side Test", bidList.getSide());
	    }
	  
	    @Test
	    public void testBidListNoArgsConstructor() {
	        BidList bidList = new BidList();
	        assertNotNull(bidList);
	        assertNull(bidList.getId()); 
	        assertNull(bidList.getAccount());
	        assertNull(bidList.getType());
	      }

	    @Test
	    public void testBidListEqualsAndHashCode() {
	        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
	        
	        BidList bid1 = new BidList(
	            1, "Acc1", "Type1", 10.0, 20.0, 15.0, 25.0, "Bench1", now, "Comm1",
	            "Sec1", "Stat1", "Trade1", "Book1", "CName1", now, "RName1", now,
	            "DName1", "DType1", "SourceId1", "Side1"
	        );
	        BidList bid2 = new BidList(
	            1, "Acc1", "Type1", 10.0, 20.0, 15.0, 25.0, "Bench1", now, "Comm1",
	            "Sec1", "Stat1", "Trade1", "Book1", "CName1", now, "RName1", now,
	            "DName1", "DType1", "SourceId1", "Side1"
	        );
	        BidList bid3 = new BidList( 
	            2, "Acc2", "Type2", 11.0, 21.0, 16.0, 26.0, "Bench2", now, "Comm2",
	            "Sec2", "Stat2", "Trade2", "Book2", "CName2", now, "RName2", now,
	            "DName2", "DType2", "SourceId2", "Side2"
	        );

	        assertEquals(bid1, bid2);
	        assertNotEquals(bid1, bid3);
	        assertEquals(bid1.hashCode(), bid2.hashCode());
	        assertNotEquals(bid1.hashCode(), bid3.hashCode());
	    }

	    @Test
	    public void testBidListToString() {
	        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
	        BidList bidList = new BidList(
	            1, "Account", "Type", 10.0, 20.0, 15.0, 25.0, "Benchmark", now, "Commentary",
	            "Security", "Status", "Trader", "Book", "CreationName", now, "RevisionName", now,
	            "DealName", "DealType", "SourceListId", "Side"
	        );
	        
	        String toStringResult = bidList.toString();
	        assertTrue(toStringResult.contains("id=1"));
	        assertTrue(toStringResult.contains("account=Account"));
	        assertTrue(toStringResult.contains("type=Type"));
	        assertTrue(toStringResult.contains("bidQuantity=10.0"));
	    }
}