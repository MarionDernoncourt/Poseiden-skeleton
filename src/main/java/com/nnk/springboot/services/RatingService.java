package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;

@Service
public class RatingService {

	private static final Logger logger = LoggerFactory.getLogger(RatingService.class);

	private final RatingRepository ratingRepository;

	public RatingService(RatingRepository ratingRepository) {
		this.ratingRepository = ratingRepository;
	}

	public List<Rating> getAllRatings() {

		logger.info("Tentative de récupération de la liste de ratings");
		List<Rating> ratings = ratingRepository.findAll();

		logger.info("Nombre de ratings : {}", ratings.size());
		return ratings;
	}

	public Rating saveRating(Rating rating) {
		logger.info("Tentative d'ajout de Rating");
		List<Rating> ratings = ratingRepository.findAll();
		ratings.stream().filter(rat -> rat.getId().equals(rating.getId()));
		return ratingRepository.save(rating);

	}

	public Rating getRatingById(Integer id) {
		logger.info("Tentative de récupération du rating : {}", id);
		Rating ratingById = ratingRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No rating with this ID"));
		return ratingById;
	}

	public Rating updateRating(Integer id, Rating rating) {
		logger.info("Tentative de mise à jour du rating Id: {}", id);

		Rating ratingToUpdate = ratingRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Aucun rating existant"));

		ratingToUpdate.setMoodysRating(rating.getMoodysRating());
		ratingToUpdate.setSandPRating(rating.getSandPRating());
		ratingToUpdate.setFitchRating(rating.getFitchRating());
		ratingToUpdate.setOrderNumber(rating.getOrderNumber());

		ratingRepository.save(ratingToUpdate);
		logger.info("Rating Id: {} mis à jour avec succès", id);
		return ratingToUpdate;

	}
	
	public void deleteRatingById(Integer id) {
		logger.info("Tentative de suppression du rating ID : {}", id);
		
		Optional<Rating> ratingOpt = ratingRepository.findById(id);
		if(ratingOpt.isEmpty()) {
			logger.warn("Aucun rating trouvé avec l'ID {}", id);
			throw new IllegalArgumentException("Aucun rating trouvé avec l'ID "+ id);
		}
		ratingRepository.deleteById(id);
		logger.info("Rating ID {} effectué avec succès", id);		

	}
}
