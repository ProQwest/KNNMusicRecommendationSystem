package edu.ucsb.cs.knn.inverted;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapFileOutputFormat;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import edu.ucsb.cs.knn.KnnDriver;
import edu.ucsb.cs.knn.types.NeighboursArrayWritable;

/**
 * The starting point for computing top <code>k</code> similar neighbors for
 * each song using the weighting formula presented in
 * "Efficient Multicore Collaborative Filtering". It execute mapper @link
 * {@link InvertedMapper} which will internally does the all-to-all comparison
 * between the songs.
 */
public class InvertedMain {// extends Configured implements Tool {

	public static final int K = 10;

	public static void main(String[] args) throws Exception {

		// SongInvertedIndexMain.main(args); //remove comment
		JobConf job = new JobConf();
		job.setJobName(InvertedMain.class.getSimpleName());
		job.setJarByClass(InvertedMain.class);
		new GenericOptionsParser(job, args);

		job.setMapRunnerClass(InvertedMapRunner.class);
		job.setMapperClass(InvertedMapper.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(NeighboursArrayWritable.class);
		job.setNumReduceTasks(0);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(NeighboursArrayWritable.class);

		job.setInputFormat(SequenceFileInputFormat.class);
		String inputDir = "sinvertedindex";
		SequenceFileInputFormat.addInputPath(job, new Path(inputDir));
		Path outputPath = new Path(job.get(KnnDriver.OUTPUT_DIR_PROPERTY));
		// FileOutputFormat.setOutputPath(job, outputPath);
		FileSystem.get(job).delete(outputPath, true);

		job.setOutputFormat(MapFileOutputFormat.class);
		MapFileOutputFormat.setOutputPath(job, outputPath);

		KnnDriver.run(job);
	}
}