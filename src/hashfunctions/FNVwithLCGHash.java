package hashfunctions;


//Taken from Orestes Bloom filter implementation

public class FNVwithLCGHash {

	
	/**
	 * @param a the byte array to be hashed
	 * @return the 32 bit integer hash value
	 */
	static int hashBytes(byte a[]) {
		// 32 bit FNV constants. Using longs as Java does not support unsigned
		// datatypes.
		final long FNV_PRIME = 16777619;
		final long FNV_OFFSET_BASIS = 2166136261l;

		if (a == null)
			return 0;

		long result = FNV_OFFSET_BASIS;
		for (byte element : a) {
			result = (result * FNV_PRIME) & 0xFFFFFFFF;
			result ^= element;
		}

		// return Arrays.hashCode(a);
		return (int) result;
	}

	public static int[] hashSimpleLCG(byte[] value, int m, int k) {
		// Java constants
		final long multiplier = 0x5DEECE66DL;
		final long addend = 0xBL;
		final long mask = (1L << 48) - 1;

		// Generate int from byte Array using the FNV hash
		int reduced = Math.abs(hashBytes(value));
		// Make number positive
		// Handle the special case: smallest negative number is itself as the
		// absolute value
		if (reduced == Integer.MIN_VALUE)
			reduced = 42;

		// Calculate hashes numbers iteratively
		int[] positions = new int[k];
		long seed = reduced;
		for (int i = 0; i < k; i++) {
			// LCG formula: x_i+1 = (multiplier * x_i + addend) mod mask
			seed = (seed * multiplier + addend) & mask;
			positions[i] = (int) (seed >>> (48 - 30)) % m;
		}
		return positions;
	}
}
