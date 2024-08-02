package me.photo.photoapi.utils;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {

    public static byte[] compressImage(byte[] data){
        //압축하는데 사용되는 클래스
        Deflater deflater = new Deflater();
        //압축 레벨을 설정, 최상의 압축률을 위해 사용
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        //입력 데이터 설정
        deflater.setInput(data);
        //압축 작업 시작
        deflater.finish();

        //압축된 데이터를 저장할 출력 스트림 준비
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];

        while(!deflater.finished()){
            //압축된 데이터를 생성
            int size = deflater.deflate(tmp);
            //출력 스트림에 쓰기
            outputStream.write(tmp,0,size);
        }

        try{
            outputStream.close();
        }catch (Exception ignored){

        }
        //압축된 데이터를 바이트 배열로 반환
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data){
        //압축 해제하는데 사용되는 클래스
        Inflater inflater = new Inflater();
        //압축 해제할 데이터를 설정
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];

        try {
            while(!inflater.finished()){
                //데이터를 압축 해제
                int count = inflater.inflate(tmp);
                //해제된 데이터를 출력 스트림에 쓰기
                outputStream.write(tmp,0,count);
            }
            outputStream.close();

        } catch (Exception ignored) {
        }
        //압축 해제된 데이터를 바이트 배열로 반환
        return outputStream.toByteArray();
    }
}
