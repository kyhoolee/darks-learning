/**
 * 
 * Copyright 2014 The Darks Learning Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.learning.test.word2vec;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.jblas.DoubleMatrix;
import org.jblas.SimpleBlas;
import org.junit.Test;

import darks.learning.common.utils.MatrixHelper;
import darks.learning.corpus.Corpus;
import darks.learning.corpus.CorpusFilter;
import darks.learning.corpus.CorpusLoader;
import darks.learning.word2vec.Word2Vec;
import darks.learning.word2vec.Word2Vec.DistanceType;
import darks.learning.word2vec.Word2Vec.Word2VecType;

public class Word2VecTest
{

	@Test
	public void testTrain()
	{
		CorpusLoader loader = new CorpusLoader();
		loader.addFilter(new CorpusFilter()
		{
			@Override
			public boolean filter(String s)
			{
				return s.length() <= 1;
			}
		});
		loader.addStopwords(new File("corpus/dic/lex-stopword.lex"));
		loader.addStopwords(new File("corpus/dic/lex-stopword1.lex"));
		Corpus corpus = loader.loadFromFile(new File("corpus/text8_limit.txt"));
		
		Word2Vec word2vec = new Word2Vec();
		word2vec.config.setTrainType(Word2VecType.CBOW)
						.setFeatureSize(200)
						.setMinVocabCount(0)
						.setWindow(5)
						.setNegative(0);
		word2vec.train(corpus);
		word2vec.saveModel(new File("test/train_data_en.model"));
	}

	@Test
	public void testDistanceEn()
	{
		Word2Vec vec = new Word2Vec();
		vec.loadModel(new File("test/train_data_en.model"));
		System.out.println(vec.distance("french"));
		System.out.println(vec.distance("chinese"));
	}

	@Test
	public void testDistance()
	{
		Word2Vec vec = new Word2Vec();
		vec.loadModel(new File("D:\\searcher\\test\\data\\dw_aps_word2vec_model_result_ch_test.txt"));
		System.out.println(vec.distance("阿里"));
		System.out.println(vec.distance("sengt"));
		double sim = vec.distance("计算机", "电脑");
		System.out.println(sim);
		List<String> sources = Arrays.asList("系统 拨打 手机".split(" "));
		List<String> targets = Arrays.asList("锁屏 系统 咨询 选择 点击 不能 安卓 淘宝 苹果 电话号码 客户 不稳定 声音 卖家 拨打 手机".split(" "));
		sim = vec.distance(sources, targets, DistanceType.STATISTIC);
		System.out.println(sim);
	}
	
}
