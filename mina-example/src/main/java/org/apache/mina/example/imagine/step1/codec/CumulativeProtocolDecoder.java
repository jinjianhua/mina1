package org.apache.mina.example.imagine.step1.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class CumulativeProtocolDecoder extends ProtocolDecoderAdapter {

    private final AttributeKey BUFFER = new AttributeKey(getClass(), "buffer");

    /**
     * Creates a new instance.
     */
    protected CumulativeProtocolDecoder() {
        // Do nothing
    }

    /**
     * Cumulates content of <tt>in</tt> into internal buffer and forwards
     * decoding request to {@link #doDecode(IoSession, IoBuffer, ProtocolDecoderOutput)}.
     * <tt>doDecode()</tt> is invoked repeatedly until it returns <tt>false</tt>
     * and the cumulative buffer is compacted after decoding ends.
     *
     * @throws IllegalStateException if your <tt>doDecode()</tt> returned
     *                               <tt>true</tt> not consuming the cumulative buffer.
     */
    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
//        System.out.println("CumulativeProtocolDecoder:decode..");
//    	if (!session.getTransportMetadata().hasFragmentation()) {
//            while (in.hasRemaining()) {
//                if (!doDecode(session, in, out)) {
//                    break;
//                }
//            }
//
//            return;
//        }
//
//        boolean usingSessionBuffer = true;
//        IoBuffer buf = (IoBuffer) session.getAttribute(BUFFER);
//        // If we have a session buffer, append data to that; otherwise
//        // use the buffer read from the network directly.
//        if (buf != null) {
//            boolean appended = false;
//            // Make sure that the buffer is auto-expanded.
//            if (buf.isAutoExpand()) {
//                try {
//                    buf.put(in);
//                    appended = true;
//                } catch (IllegalStateException e) {
//                    // A user called derivation method (e.g. slice()),
//                    // which disables auto-expansion of the parent buffer.
//                } catch (IndexOutOfBoundsException e) {
//                    // A user disabled auto-expansion.
//                }
//            }
//
//            if (appended) {
//                buf.flip();
//            } else {
//                // Reallocate the buffer if append operation failed due to
//                // derivation or disabled auto-expansion.
//                buf.flip();
//                IoBuffer newBuf = IoBuffer.allocate(buf.remaining() + in.remaining()).setAutoExpand(true);
//                newBuf.order(buf.order());
//                newBuf.put(buf);
//                newBuf.put(in);
//                newBuf.flip();
//                buf = newBuf;
//
//                // Update the session attribute.
//                session.setAttribute(BUFFER, buf);
//            }
//        } else {
//            buf = in;
//            usingSessionBuffer = false;
//        }
//
//        for (;;) {
//            int oldPos = buf.position();
            boolean decoded = doDecode(session, in, out);
//            if (decoded) {
//                if (buf.position() == oldPos) {
//                    throw new IllegalStateException("doDecode() can't return true when buffer is not consumed.");
//                }
//
//                if (!buf.hasRemaining()) {
//                    break;
//                }
//            } else {
//                break;
//            }
//        }
//
//        // if there is any data left that cannot be decoded, we store
//        // it in a buffer in the session and next time this decoder is
//        // invoked the session buffer gets appended to
//        if (buf.hasRemaining()) {
//            if (usingSessionBuffer && buf.isAutoExpand()) {
//                buf.compact();
//            } else {
//                storeRemainingInSession(buf, session);
//            }
//        } else {
//            if (usingSessionBuffer) {
//                removeSessionBuffer(session);
//            }
//        }
    }

    /**
     * Implement this method to consume the specified cumulative buffer and
     * decode its content into message(s).
     *
     * @param in the cumulative buffer
     * @return <tt>true</tt> if and only if there's more to decode in the buffer
     *         and you want to have <tt>doDecode</tt> method invoked again.
     *         Return <tt>false</tt> if remaining data is not enough to decode,
     *         then this method will be invoked again when more data is cumulated.
     * @throws Exception if cannot decode <tt>in</tt>.
     */
    protected abstract boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception;

    /**
     * Releases the cumulative buffer used by the specified <tt>session</tt>.
     * Please don't forget to call <tt>super.dispose( session )</tt> when
     * you override this method.
     */
    @Override
    public void dispose(IoSession session) throws Exception {
        removeSessionBuffer(session);
    }

    private void removeSessionBuffer(IoSession session) {
        session.removeAttribute(BUFFER);
    }

    private void storeRemainingInSession(IoBuffer buf, IoSession session) {
        final IoBuffer remainingBuf = IoBuffer.allocate(buf.capacity()).setAutoExpand(true);

        remainingBuf.order(buf.order());
        remainingBuf.put(buf);

        session.setAttribute(BUFFER, remainingBuf);
    }
}

