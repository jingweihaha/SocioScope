package com.zjw.spark;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

/**
*@author:Jingwei Zhang
*@email:jingweizhangcs@gmail.com
*@version:2017年5月7日上午10:59:58
*
*This class contains methods receiving messages from twitter
*/
public class Servlet {
	public static void main(String [] args) throws Exception
	{
		SparkConf conf = new SparkConf().setMaster("local[2]")
				.setAppName("NetworkWordCount");
		
		//create a java streaming context, specifiy the duration 1s
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
		//creat DStream (discrete stream)
		//each twitter is a instance of DStream, it is received from localhost:9999
		JavaReceiverInputDStream<String> twitter = jssc.socketTextStream("localhost", 9999);
		
		//split each words
		JavaDStream<String> words = twitter.flatMap(
				new FlatMapFunction<String, String>() {
					@Override
					public Iterator<String> call(String x) throws Exception {
						return Arrays.asList(x.split(" ")).iterator();
			}
		}
	    );
		
		//word count map
		JavaPairDStream<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String t) throws Exception {
				// TODO Auto-generated method stub
				return new Tuple2<String, Integer>(t, 1);
			}
		});
		JavaPairDStream<String, Integer> wordCounts  = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception{
				return new Integer(v1+v2);
			}
		});	
		wordCounts.print();
		
		jssc.start();
		jssc.awaitTermination();
		
	}
}
