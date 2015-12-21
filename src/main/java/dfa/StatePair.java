package dfa;

import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class StatePair {
	public final State a;
	public final State b;
	private final static LoadingCache<String, StatePair> PAIRS = CacheBuilder.newBuilder()
		       .maximumSize(1000)
		       .build(
		           new CacheLoader<String, StatePair>() {
		             public StatePair load(String key) {
		            	 String[] split = key.split(":");
		               String _a = split[0];
		               String _b = split[1];
					return new StatePair(State.valueOf(_a), State.valueOf(_b));
		             }
		           });
	private  StatePair(State a, State b) {
		this.a = a;
		this.b = b;
	}
	public static StatePair valueOf(String a, String b){
		try {
			return PAIRS.get(MessageFormat.format("{0}:{1}",a,b));
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
}
