package org.fusion.x11.nativeHelpers;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestNativeBufferHelper {

	private NativeBufferHelper  nativeBufferHelper;

	private static final byte[] bufferContents = {
	                (byte) 0x80,
	                // end byte read (byte size = 1)
	                (byte) 0x80,
	                // end short read (short size = 2)
	                (byte) 0x00,
	                (byte) 0x80,
	                // end int read (int size = 4)
	                (byte) 0x00, (byte) 0x00, (byte) 0x00,
	                (byte) 0x80,
	                // end long read (long size = 8)
	                (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF,
	                (byte) 0x00, (byte) 0xFF, (byte) 0x00, (byte) 0xFF,
	                (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

	@Before
	public void setup() throws IOException {
		this.nativeBufferHelper = NativeBufferHelper.getBufferHelper();
		this.nativeBufferHelper.getBuffer().clear();
		this.nativeBufferHelper.getBuffer()
		                .put(TestNativeBufferHelper.bufferContents);
		this.nativeBufferHelper.getBuffer().rewind();
	}

	@Test
	public void testUnsignedByteRead() {
		final int unsignedByte = this.nativeBufferHelper.readUnsignedByte();
		Assert.assertEquals(128,
		                    unsignedByte);
	}

	@Test
	public void testSignedByteRead() {
		final int signedByte = this.nativeBufferHelper.readSignedByte();
		Assert.assertEquals(-128,
		                    signedByte);
	}

	@Test
	public void testUnsignedShortRead() {
		final int unsignedShort = this.nativeBufferHelper.readUnsignedShort();
		Assert.assertEquals(32896,
		                    unsignedShort);
	}

	@Test
	public void testSignedShortRead() {
		final int signedShort = this.nativeBufferHelper.readSignedShort();
		Assert.assertEquals(-32640,
		                    signedShort);
	}

	@Test
	public void testUnsginedIntRead() {
		final long unsignedInt = this.nativeBufferHelper.readUnsignedInt();
		Assert.assertEquals(2147516544l,
		                    unsignedInt);
	}

	@Test
	public void testSignedIntRead() {
		final int signedInt = this.nativeBufferHelper.readSignedInt();
		Assert.assertEquals(-2147450752,
		                    signedInt);
	}

	@Test
	public void testSignedLongRead() {
		final long signedLong = this.nativeBufferHelper.readSignedLong();
		Assert.assertEquals(-9223372034707259264l,
		                    signedLong);
	}

	@Test
	public void testMixedReads() {
		// TODO
	}

	@Test
	public void testReadConcurrencySameThread() {
		// TODO
	}

	@Test
	public void testReadConcurrencyDifferentThreads() {
		// TODO
	}
}
