package hashfunctions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Random;

//Taken from Orestes-Bloomfilter implementation

public class SHA256Hash {
	
	private static final int seed32 = 89478583;

	public static int[] hashCrypt(byte[] value, int m, int k) {
		//MessageDigest is not thread-safe --> use new instance
		MessageDigest cryptHash = null;
		try {
			cryptHash = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		int[] positions = new int[k];

		int computedHashes = 0;
		// Add salt to the hash deterministically in order to generate different
		// hashes for each round
		// Alternative: use pseudorandom sequence
		Random r = new Random(seed32);
		byte[] digest = new byte[0];
		while (computedHashes < k) {
			// byte[] saltBytes =
			// ByteBuffer.allocate(4).putInt(r.nextInt()).array();
			cryptHash.update(digest);
			digest = cryptHash.digest(value);
			BitSet hashed = BitSet.valueOf(digest);

			// Convert the hash to numbers in the range [0,size)
			// Size of the BloomFilter rounded to the next power of two
			int filterSize = 32 - Integer.numberOfLeadingZeros(m);
			// Computed hash bits
			int hashBits = digest.length * 8;
			// Split the hash value according to the size of the Bloomfilter --> higher performance than just doing modulo
			for (int split = 0; split < (hashBits / filterSize)
					&& computedHashes < k; split++) {
				int from = split * filterSize;
				int to = (split + 1) * filterSize;
				BitSet hashSlice = hashed.get(from, to);
				// Bitset to Int
				long[] longHash = hashSlice.toLongArray();
				int intHash = longHash.length > 0 ? (int) longHash[0] : 0;
				// Only use the position if it's in [0,size); Called rejection sampling
				if (intHash < m) {
					positions[computedHashes] = intHash;
					computedHashes++;
				}
			}
		}

		return positions;
	}
}
