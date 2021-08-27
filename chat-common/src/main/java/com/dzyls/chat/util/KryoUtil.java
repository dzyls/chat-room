package com.dzyls.chat.util;

import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/26 19:40
 * @Version 1.0.0
 * @Description:
 */
public class KryoUtil {

    private static final UUIDSerializer uuidSerializer = new UUIDSerializer();

    private static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(UUID.class, uuidSerializer);
            JavaSerializer serializer = new JavaSerializer();
            kryo.register(ReentrantReadWriteLock.class,serializer);
            kryo.register(CommonRequest.class);
            kryo.register(OperationType.class);
            return kryo;
        }
    };

    public static Kryo getKryo() {
        return kryoLocal.get();
    }

    public static byte[] kryoSerialize(Object obj) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Output output = new Output(bout);
        kryoLocal.get().writeObject(output, obj);
        output.close();
        return bout.toByteArray();
    }

    public static <T> T kryoDeserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        return kryoLocal.get().readObject(new Input(bin), clazz);
    }

    public static Object readClassAndObject(byte[] bytes) {
        return kryoLocal.get().readClassAndObject(new ByteBufferInput(bytes));
    }

    private static class UUIDSerializer extends Serializer<UUID> {

        UUIDSerializer() {
            setImmutable(true);
        }

        @Override
        public void write(final Kryo kryo, final Output output, final UUID uuid) {
            output.writeLong(uuid.getMostSignificantBits());
            output.writeLong(uuid.getLeastSignificantBits());
        }

        @Override
        public UUID read(Kryo kryo, Input input, Class<? extends UUID> aClass) {
            return new UUID(input.readLong(), input.readLong());
        }

    }

    public static void main(String[] args) {
        byte[] bytes = KryoUtil.kryoSerialize(CommonRequest.generateSendRequest("123"));
        System.out.println(KryoUtil.kryoDeserialize(bytes, CommonRequest.class));
    }

}
