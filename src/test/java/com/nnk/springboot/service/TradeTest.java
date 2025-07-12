package com.nnk.springboot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;

@ExtendWith(MockitoExtension.class)
public class TradeTest {

	@Mock
	private TradeRepository tradeRepository;

	@InjectMocks
	private TradeService tradeService;

	private Trade trade;

	@BeforeEach
	void setUp() {
		trade = new Trade();
		trade.setId(1);
		trade.setAccount("TradeAccount");
		trade.setType("TypeTest");
	}

	@Test
	public void testGetAllTrade_WhenSuccess() {
		List<Trade> trades = new ArrayList<>();
		trades.add(trade);

		when(tradeRepository.findAll()).thenReturn(trades);

		List<Trade> foundTrades = tradeService.getAllTrades();

		assertEquals(trades, foundTrades);
		assertEquals(trades.size(), foundTrades.size());
	}

	@Test
	public void testSaveTrade_WhenSuccess() {
		when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

		Trade savedTrade = tradeService.saveTrade(trade);

		assertEquals(trade.getAccount(), savedTrade.getAccount());
		assertEquals(trade.getType(), savedTrade.getType());
		verify(tradeRepository, times(1)).save(any(Trade.class));
	}

	@Test
	public void testSaveTrade_WhenException() {
		doThrow(new RuntimeException("Erreur lors de la sauvegarde")).when(tradeRepository).save(any(Trade.class));

		assertThrows(RuntimeException.class, () -> {
			tradeService.saveTrade(trade);
		});
	}

	@Test
	public void testGetTradeById_WhenSuccess() {
		when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(trade));

		Trade foundTrade = tradeService.getTradeById(1);

		assertEquals(trade.getAccount(), foundTrade.getAccount());
		assertEquals(trade.getType(), foundTrade.getType());
		verify(tradeRepository, times(1)).findById(anyInt());
	}

	@Test
	public void testGetTradeById_WhenNotFound() {
		when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			tradeService.getTradeById(5);
		});
		verify(tradeRepository, times(1)).findById(anyInt());
	}
	
	@Test
	public void testUpdateTrade_WhenSuccess() {
		when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(trade));
		
		Trade newTradeData = new Trade();
		newTradeData.setAccount("newAccountData");
		newTradeData.setType("NewTypeData");
		
		Trade expectedSavedTrade = new Trade();
		expectedSavedTrade.setAccount(trade.getAccount());
		expectedSavedTrade.setType(trade.getType());
		expectedSavedTrade.setId(trade.getId());
		
		when(tradeRepository.save(any(Trade.class))).thenReturn(expectedSavedTrade);
		
		Trade actualUpdatedTrade = tradeService.updateTradeById(trade.getId(), newTradeData);
		
		assertEquals(expectedSavedTrade.getAccount(), actualUpdatedTrade.getAccount());
		assertEquals(expectedSavedTrade.getType(), actualUpdatedTrade.getType());
		verify(tradeRepository, times(1)).findById(anyInt());
		verify(tradeRepository, times(1)).save(any(Trade.class));
		}
	
	@Test
	public void testUpdateTrade_WhenExceptionDuringSave() {
		when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(trade));
		
		Trade newTradeData = new Trade();
		newTradeData.setAccount("newAccountData");
		newTradeData.setType("NewTypeData");
		
		doThrow(new RuntimeException("Erreur lors de la sauvegarde de la mise à jour")).when(tradeRepository).save(any(Trade.class));
		
		assertThrows(RuntimeException.class, () -> {
			tradeService.updateTradeById(trade.getId(), newTradeData);
		});
		verify(tradeRepository, times(1)).findById(anyInt());
		verify(tradeRepository, times(1)).save(any(Trade.class));
		}
	
	@Test
	public void testUpdateTrade_WhenNotFound() {
		when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		Trade newTradeData = new Trade();
		newTradeData.setAccount("newAccountData");
		newTradeData.setType("NewTypeData");
		
		assertThrows(IllegalArgumentException.class, () -> {
			tradeService.updateTradeById(trade.getId(), newTradeData);
		});
		verify(tradeRepository, times(1)).findById(anyInt());
		verify(tradeRepository, never()).save(any(Trade.class));
		}
	
	@Test
	public void testDeleteTrade_WhenSuccess() {
		when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(trade));
		
		tradeService.deleteTradeById(trade.getId());
		
		verify(tradeRepository, times(1)).findById(anyInt());
		verify(tradeRepository, times(1)).delete(any(Trade.class));
	}
	
	@Test
	public void testDeleteTrade_WhenNotFound() {
		when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			tradeService.deleteTradeById(5);
			});
		
		verify(tradeRepository, times(1)).findById(anyInt());
		verify(tradeRepository, never()).delete(any(Trade.class));
	}

	    @Test
	    public void testTradeGettersAndSetters() {
	        Trade trade = new Trade();

	        trade.setId(1);
	        trade.setAccount("Account Test");
	        trade.setType("Type Test");
	        trade.setBuyQuantity(10.0);
	        trade.setSellQuantity(20.0);
	        trade.setBuyPrice(15.0);
	        trade.setSellPrice(25.0);
	        trade.setBenchmark("Benchmark Test");
	        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
	        trade.setTradeDate(now);
	        trade.setSecurity("Security Test");
	        trade.setStatus("Status Test");
	        trade.setTrader("Trader Test");
	        trade.setBook("Book Test");
	        trade.setCreationName("Creation Name Test");
	        trade.setCreationDate(now);
	        trade.setRevisionName("Revision Name Test");
	        trade.setRevisionDate(now);
	        trade.setDealName("Deal Name Test");
	        trade.setDealType("Deal Type Test");
	        trade.setSourceListId("Source List ID Test");
	        trade.setSide("Side Test");

	        assertEquals(1, trade.getId());
	        assertEquals("Account Test", trade.getAccount());
	        assertEquals("Type Test", trade.getType());
	        assertEquals(10.0, trade.getBuyQuantity());
	        assertEquals(20.0, trade.getSellQuantity());
	        assertEquals(15.0, trade.getBuyPrice());
	        assertEquals(25.0, trade.getSellPrice());
	        assertEquals("Benchmark Test", trade.getBenchmark());
	        assertEquals(now, trade.getTradeDate());
	        assertEquals("Security Test", trade.getSecurity());
	        assertEquals("Status Test", trade.getStatus());
	        assertEquals("Trader Test", trade.getTrader());
	        assertEquals("Book Test", trade.getBook());
	        assertEquals("Creation Name Test", trade.getCreationName());
	        assertEquals(now, trade.getCreationDate());
	        assertEquals("Revision Name Test", trade.getRevisionName());
	        assertEquals(now, trade.getRevisionDate());
	        assertEquals("Deal Name Test", trade.getDealName());
	        assertEquals("Deal Type Test", trade.getDealType());
	        assertEquals("Source List ID Test", trade.getSourceListId());
	        assertEquals("Side Test", trade.getSide());
	    }
	    
	    @Test
	    public void testTradeEqualsAndHashCode() {
	        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
	        
	        Trade trade1 = new Trade(
	            1, "Acc1", "Type1", 10.0, 20.0, 15.0, 25.0, "Bench1", now, "Sec1",
	            "Stat1", "Trade1", "Book1", "CName1", now, "RName1", now,
	            "DName1", "DType1", "SourceId1", "Side1"
	        );
	        Trade trade2 = new Trade(
	            1, "Acc1", "Type1", 10.0, 20.0, 15.0, 25.0, "Bench1", now, "Sec1",
	            "Stat1", "Trade1", "Book1", "CName1", now, "RName1", now,
	            "DName1", "DType1", "SourceId1", "Side1"
	        );
	        Trade trade3 = new Trade( // ID différent
	            2, "Acc2", "Type2", 11.0, 21.0, 16.0, 26.0, "Bench2", now, "Sec2",
	            "Stat2", "Trade2", "Book2", "CName2", now, "RName2", now,
	            "DName2", "DType2", "SourceId2", "Side2"
	        );

	        assertEquals(trade1, trade2);
	        assertNotEquals(trade1, trade3);
	        assertEquals(trade1.hashCode(), trade2.hashCode());
	        assertNotEquals(trade1.hashCode(), trade3.hashCode()); 
	    }
	
}
