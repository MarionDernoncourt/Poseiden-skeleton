package com.nnk.springboot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;

@ExtendWith(MockitoExtension.class)
public class CurvePointTest {

	@Mock
	private CurvePointRepository curvePointRepository;

	@InjectMocks
	private CurvePointService curvePointService;

	private CurvePoint curvePoint;

	@BeforeEach
	void setUp() {
		curvePoint = new CurvePoint();
		curvePoint.setId(1);
		curvePoint.setCurveId(10);
		curvePoint.setTerm(10.0);
		curvePoint.setValue(30.0);
	}

	@Test
	public void testGetAllCurvePoint_WhenSuccess() {

		when(curvePointRepository.findAll()).thenReturn(List.of(curvePoint));

		List<CurvePoint> curvePoints = curvePointService.getAllCurvePoints();

		assertEquals(1, curvePoints.size());
		verify(curvePointRepository, times(1)).findAll();
	}

	@Test
	public void testGetAllCurvePoint_WhenException() {
		doThrow(new RuntimeException("Erreur lors de la récupération des curvepoints")).when(curvePointRepository)
				.findAll();

		assertThrows(RuntimeException.class, () -> {
			curvePointService.getAllCurvePoints();
		});
		verify(curvePointRepository, times(1)).findAll();
	}

	@Test
	public void testSaveCurvePoint_WhenSuccess() {
		when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);

		CurvePoint savedCurvePoint = curvePointService.saveCurvePoint(curvePoint);

		assertEquals(curvePoint.getTerm(), savedCurvePoint.getTerm());
		assertEquals(curvePoint.getValue(), savedCurvePoint.getValue());
		verify(curvePointRepository, times(1)).save(any(CurvePoint.class));
	}

	@Test
	public void testSaveCurvePoint_WhenException() {

		doThrow(new RuntimeException("Erreur lors de la sauvegarde")).when(curvePointRepository)
				.save(any(CurvePoint.class));

		assertThrows(RuntimeException.class, () -> {
			curvePointService.saveCurvePoint(curvePoint);
		});
	}
	
	@Test
	public void testGetCurvePointById_WhenSuccess() {
		when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePoint));
		
		CurvePoint foundCurvePoint = curvePointService.getCurvePointById(1);
		
		assertNotNull(foundCurvePoint);
		verify(curvePointRepository, times(1)).findById(anyInt());
	}
	
	@Test
	public void testGetCurvePointById_WhenNotFound() {
		when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			curvePointService.getCurvePointById(5);
		});
		verify(curvePointRepository, times(1)).findById(anyInt());
	}
	
	@Test
	public void testUpdateCurvePoint_WhenSuccess() {
		when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePoint));

		CurvePoint newCurveData = new CurvePoint();
		newCurveData.setTerm(20.0);
		newCurveData.setValue(20.0); 

		CurvePoint expectedSavedCurvePoint = new CurvePoint();
		expectedSavedCurvePoint.setId(curvePoint.getId()); 
		expectedSavedCurvePoint.setTerm(40.00);
		expectedSavedCurvePoint.setValue(10.0);

		when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(expectedSavedCurvePoint);

		CurvePoint actualUpdatedCurvePoint = curvePointService.updateCurvePoint(curvePoint.getId(), newCurveData);

		assertEquals(expectedSavedCurvePoint.getTerm(), actualUpdatedCurvePoint.getTerm());
		assertEquals(expectedSavedCurvePoint.getValue(), actualUpdatedCurvePoint.getValue());
		assertEquals(expectedSavedCurvePoint.getId(), actualUpdatedCurvePoint.getId()); 
	}
	
	@Test
	public void testUpdateCurvePoint_WhenExceptionDuringSave() {
		when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePoint));
		
		CurvePoint newCurveData = new CurvePoint();
		newCurveData.setTerm(20.0);
		newCurveData.setValue(20.0); 
		
		doThrow(new RuntimeException("Erreur lors de la sauvegarde de la mise à jour")).when(curvePointRepository).save(newCurveData);
		
		assertThrows(RuntimeException.class, () -> {
			curvePointService.updateCurvePoint(curvePoint.getId(), newCurveData);
		});
		verify(curvePointRepository, times(1)).findById(anyInt());
		verify(curvePointRepository, times(1)).save(any(CurvePoint.class));
	}
	
	@Test
	public void testUpdateCurvePoint_WhenNotFound() {
		when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		CurvePoint newCurveData = new CurvePoint();
		newCurveData.setTerm(20.0);
		newCurveData.setValue(20.0); 
		
		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			curvePointService.updateCurvePoint(1, newCurveData);
		});
		
		assertEquals("Aucun curvePoint trouvé", thrown.getMessage());
		verify(curvePointRepository, times(1)).findById(anyInt());
		verify(curvePointRepository, never()).save(any(CurvePoint.class));
	}
	
	@Test
	public void testDeleteCurvePointById_WhenSuccess() {
		when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePoint));
		
		curvePointService.deleteCurvePointById(curvePoint.getId());
		
		verify(curvePointRepository, times(1)).findById(anyInt());
		verify(curvePointRepository, times(1)).delete(curvePoint);
	}
	
	@Test
	public void testDeleteCurvePointById_WhenNotFound() {
		when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());
		
		assertThrows(IllegalArgumentException.class, () -> {
			curvePointService.deleteCurvePointById(999);
		}) ;
		verify(curvePointRepository, times(1)).findById(anyInt());
		verify(curvePointRepository, never()).deleteById(anyInt());
	}
	
	  @Test
	    public void testCurvePointGettersAndSetters() {
	        CurvePoint curvePoint2 = new CurvePoint();

	        curvePoint2.setId(1);
	        curvePoint2.setCurveId(10);
	        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
	        curvePoint2.setAsOfDate(now);
	        curvePoint2.setTerm(1.5);
	        curvePoint2.setValue(100.75);
	        curvePoint2.setCreationDate(now);

	        assertEquals(1, curvePoint2.getId());
	        assertEquals(10, curvePoint2.getCurveId());
	        assertEquals(now, curvePoint2.getAsOfDate());
	        assertEquals(1.5, curvePoint2.getTerm());
	        assertEquals(100.75, curvePoint2.getValue());
	        assertEquals(now, curvePoint2.getCreationDate());
	    }
	  
	  @Test
	    public void testCurvePointEqualsAndHashCode() {
	        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
	        
	        CurvePoint curve1 = new CurvePoint(1, 10, now, 1.5, 100.0, now);
	        CurvePoint curve2 = new CurvePoint(1, 10, now, 1.5, 100.0, now);
	        CurvePoint curve3 = new CurvePoint(2, 20, now, 2.5, 200.0, now); // Different ID

	        // Test equality
	        assertEquals(curve1, curve2);
	        assertNotEquals(curve1, curve3);
	        
	        // Test hashCode
	        assertEquals(curve1.hashCode(), curve2.hashCode());
	        assertNotEquals(curve1.hashCode(), curve3.hashCode()); 
	    }
}
