/*
 * LensKit, a reference implementation of recommender algorithms.
 * Copyright 2010-2011 Regents of the University of Minnesota
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.grouplens.lenskit.slopeone;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.grouplens.lenskit.data.Rating;
import org.grouplens.lenskit.data.SimpleRating;
import org.grouplens.lenskit.data.dao.RatingCollectionDAO;
import org.grouplens.lenskit.data.snapshot.PackedRatingSnapshot;
import org.grouplens.lenskit.slopeone.SlopeOneModel;
import org.grouplens.lenskit.slopeone.SlopeOneModelBuilder;
import org.junit.Test;

public class TestSlopeOneModelBuilder {

	public static final double EPSILON = 1.0e-6;
	
	@Test
	public void testBuild1() {

		List<Rating> rs = new ArrayList<Rating>();
		rs.add(new SimpleRating(1, 5, 2));
		rs.add(new SimpleRating(2, 5, 4));
		rs.add(new SimpleRating(1, 3, 5));
		rs.add(new SimpleRating(2, 3, 4));
		RatingCollectionDAO.Manager manager = new RatingCollectionDAO.Manager(rs);
		PackedRatingSnapshot.Builder snapBuilder = new PackedRatingSnapshot.Builder(manager.open());
		PackedRatingSnapshot snapshot = snapBuilder.build();		
		SlopeOneModelBuilder builder1 = new SlopeOneModelBuilder();
		builder1.setRatingSnapshot(snapshot);
		builder1.setDamping(0);
		SlopeOneModel model1 = builder1.build();
		assertEquals(2, model1.getCoratings(5, 3));
		assertEquals(2, model1.getCoratings(3, 5));
		assertEquals(-1.5, model1.getDeviation(5, 3),EPSILON);
		assertEquals(1.5, model1.getDeviation(3,5), EPSILON);
	}

	@Test
	public void testBuild2() {

		List<Rating> rs = new ArrayList<Rating>();
		rs.add(new SimpleRating(1, 4, 4));
		rs.add(new SimpleRating(2, 4, 5));
		rs.add(new SimpleRating(3, 4, 4));
		rs.add(new SimpleRating(1, 5, 3));
		rs.add(new SimpleRating(2, 5, 5));
		rs.add(new SimpleRating(3, 5, 1));
		rs.add(new SimpleRating(1, 6, 1));
		rs.add(new SimpleRating(2, 6, 5));
		rs.add(new SimpleRating(3, 6, 3));
		RatingCollectionDAO.Manager manager = new RatingCollectionDAO.Manager(rs);
		PackedRatingSnapshot.Builder snapBuilder = new PackedRatingSnapshot.Builder(manager.open());
		PackedRatingSnapshot snapshot = snapBuilder.build();		
		SlopeOneModelBuilder builder2 = new SlopeOneModelBuilder();
		builder2.setRatingSnapshot(snapshot);
		builder2.setDamping(0);
		SlopeOneModel model2 = builder2.build();
		assertEquals(3, model2.getCoratings(4, 5));
		assertEquals(3, model2.getCoratings(5, 4));
		assertEquals(3, model2.getCoratings(4, 6));
		assertEquals(3, model2.getCoratings(6, 4));
		assertEquals(3, model2.getCoratings(5, 6));
		assertEquals(3, model2.getCoratings(6, 5));
		assertEquals(4/3.0, model2.getDeviation(4, 6), EPSILON);
		assertEquals(-4/3.0, model2.getDeviation(6, 4), EPSILON);
		assertEquals(4/3.0, model2.getDeviation(4, 5), EPSILON);
		assertEquals(-4/3.0, model2.getDeviation(5, 4), EPSILON);
		assertEquals(0, model2.getDeviation(5, 6), EPSILON);
		assertEquals(0, model2.getDeviation(6, 5), EPSILON);
	}

	@Test
	public void testBuild3() {

		List<Rating> rs = new ArrayList<Rating>();
		rs.add(new SimpleRating(1, 6, 4));
		rs.add(new SimpleRating(2, 6, 2));
		rs.add(new SimpleRating(1, 7, 3));
		rs.add(new SimpleRating(2, 7, 2));
		rs.add(new SimpleRating(3, 7, 5));
		rs.add(new SimpleRating(4, 7, 2));
		rs.add(new SimpleRating(1, 8, 3));
		rs.add(new SimpleRating(2, 8, 4));
		rs.add(new SimpleRating(3, 8, 3));
		rs.add(new SimpleRating(4, 8, 2));
		rs.add(new SimpleRating(5, 8, 3));
		rs.add(new SimpleRating(6, 8, 2));
		rs.add(new SimpleRating(1, 9, 3));
		rs.add(new SimpleRating(3, 9, 4));
		RatingCollectionDAO.Manager manager = new RatingCollectionDAO.Manager(rs);
		PackedRatingSnapshot.Builder snapBuilder = new PackedRatingSnapshot.Builder(manager.open());
		PackedRatingSnapshot snapshot = snapBuilder.build();		
		SlopeOneModelBuilder builder3 = new SlopeOneModelBuilder();
		builder3.setRatingSnapshot(snapshot);
		builder3.setDamping(0);
		SlopeOneModel model3 = builder3.build();
		assertEquals(2, model3.getCoratings(6, 7));
		assertEquals(2, model3.getCoratings(7, 6));
		assertEquals(2, model3.getCoratings(6, 8));
		assertEquals(2, model3.getCoratings(8, 6));
		assertEquals(1, model3.getCoratings(6, 9));
		assertEquals(1, model3.getCoratings(9, 6));
		assertEquals(4, model3.getCoratings(7, 8));
		assertEquals(4, model3.getCoratings(8, 7));
		assertEquals(2, model3.getCoratings(7, 9));
		assertEquals(2, model3.getCoratings(9, 7));
		assertEquals(2, model3.getCoratings(8, 9));
		assertEquals(2, model3.getCoratings(9, 8));
		assertEquals(0.5, model3.getDeviation(6, 7), EPSILON);
		assertEquals(-0.5, model3.getDeviation(7, 6), EPSILON);
		assertEquals(-0.5, model3.getDeviation(6, 8), EPSILON);
		assertEquals(0.5, model3.getDeviation(8, 6), EPSILON);
		assertEquals(1, model3.getDeviation(6, 9), EPSILON);
		assertEquals(-1, model3.getDeviation(9, 6), EPSILON);
		assertEquals(0, model3.getDeviation(7, 8), EPSILON);
		assertEquals(0, model3.getDeviation(8, 7), EPSILON);
		assertEquals(0.5, model3.getDeviation(7, 9), EPSILON);
		assertEquals(-0.5, model3.getDeviation(9, 7), EPSILON);
		assertEquals(-0.5, model3.getDeviation(8, 9), EPSILON);
		assertEquals(0.5, model3.getDeviation(9, 8), EPSILON);
	}
	
	@Test
	public void testBuild4() {
		List<Rating> rs = new ArrayList<Rating>();
		rs.add(new SimpleRating(1, 4, 3.5));
		rs.add(new SimpleRating(2, 4, 5));
		rs.add(new SimpleRating(3, 5, 4.25));
		rs.add(new SimpleRating(2, 6, 3));
		rs.add(new SimpleRating(1, 7, 4));
		rs.add(new SimpleRating(2, 7, 4));
		rs.add(new SimpleRating(3, 7, 1.5));
		RatingCollectionDAO.Manager manager = new RatingCollectionDAO.Manager(rs);
		PackedRatingSnapshot.Builder snapBuilder = new PackedRatingSnapshot.Builder(manager.open());
		PackedRatingSnapshot snap = snapBuilder.build();
		SlopeOneModelBuilder builder4 = new SlopeOneModelBuilder();
		builder4.setRatingSnapshot(snap);
		builder4.setDamping(0);
		SlopeOneModel model4 = builder4.build();
		assertEquals(0, model4.getCoratings(4, 5));
		assertEquals(0, model4.getCoratings(5, 4));
		assertEquals(1, model4.getCoratings(4, 6));
		assertEquals(1, model4.getCoratings(6, 4));
		assertEquals(2, model4.getCoratings(4, 7));
		assertEquals(2, model4.getCoratings(7, 4));
		assertEquals(0, model4.getCoratings(5, 6));
		assertEquals(0, model4.getCoratings(6, 5));
		assertEquals(1, model4.getCoratings(5, 7));
		assertEquals(1, model4.getCoratings(7, 5));
		assertEquals(1, model4.getCoratings(6, 7));
		assertEquals(1, model4.getCoratings(7, 6));
		assertEquals(Double.NaN, model4.getDeviation(4, 5), 0);
		assertEquals(Double.NaN, model4.getDeviation(5, 4), 0);
		assertEquals(2, model4.getDeviation(4, 6), EPSILON);
		assertEquals(-2, model4.getDeviation(6, 4), EPSILON);
		assertEquals(0.25, model4.getDeviation(4, 7), EPSILON);
		assertEquals(-0.25, model4.getDeviation(7, 4), EPSILON);
		assertEquals(Double.NaN, model4.getDeviation(5, 6), 0);
		assertEquals(Double.NaN, model4.getDeviation(6, 5), 0);
		assertEquals(2.75, model4.getDeviation(5, 7), EPSILON);
		assertEquals(-2.75, model4.getDeviation(7, 5), EPSILON);
		assertEquals(-1, model4.getDeviation(6, 7), EPSILON);
		assertEquals(1, model4.getDeviation(7, 6), EPSILON);
	}
}