package me.own.learn.commons.base.utils.image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/3/16.
 */
public class ImageRotateUtils {

    private static Logger logger = LoggerFactory.getLogger(ImageRotateUtils.class);

    public static int getRotateDegree(int orientation) {
        int degree = 0;
        if (orientation == 6) {
            degree = 90;
        } else if (orientation == 3) {
            degree = 180;
        } else if (orientation == 8) {
            degree = 270;
        }

        return degree;
    }

    public static BufferedImage rotateImageIfNecessary(BufferedImage bi, File imageFile) throws IOException{
        return rotateImageIfNecessary(bi, imageFile, false);
    }

    /**
     * 根据EXIF信息旋转图片
     * @param bi
     * @param imageFile
     * @param rewriteSourceFile 是否需要修改源文件
     * @return
     * @throws IOException
     */
    public static BufferedImage rotateImageIfNecessary(BufferedImage bi, File imageFile, boolean rewriteSourceFile) throws IOException {
        try {
            boolean isRotated = false;
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            Iterable<Directory> metaStr = metadata.getDirectories();
            for (Iterator<Directory> direcIter = metaStr.iterator(); direcIter.hasNext();) {
                Directory directory = direcIter.next();
                if (directory.getClass().equals(ExifIFD0Directory.class)) {
                    Object orientation = directory.getObject(274);
                    if (orientation != null) {
                        int ori = (int)orientation;
                        //顺时针旋转一定角度
                        if (ori == 6) {
                            bi = rotateImage(bi, 90);
                            isRotated = true;
                        } else if (ori == 3) {
                            bi = rotateImage(bi, 90);
                            bi = rotateImage(bi, 90);
                            isRotated = true;
                        } else if (ori == 8) {
                            bi = rotateImage(bi, 90);
                            bi = rotateImage(bi, 90);
                            bi = rotateImage(bi, 90);
                            isRotated = true;
                        }
                    }
                }
            }

            if (rewriteSourceFile && isRotated){
                String[] arr = imageFile.getName().split("\\.");
                ImageIO.write(bi, arr[arr.length - 1], imageFile);
            }

        } catch (ImageProcessingException e) {
        } catch (IOException e) {
        } catch (Exception e){
        }

        return bi;
    }

    public static BufferedImage rotateImage(BufferedImage bufferedimage, final int degree) {
        if(degree == 90){
            return rotateImageBy90(bufferedimage);
        }else if(degree == 180){
            bufferedimage = rotateImageBy90(bufferedimage);
            bufferedimage = rotateImageBy90(bufferedimage);
            return bufferedimage;
        }else if(degree == 270){
            bufferedimage = rotateImageBy90(bufferedimage);
            bufferedimage = rotateImageBy90(bufferedimage);
            bufferedimage = rotateImageBy90(bufferedimage);
            return bufferedimage;
        }else{
            return bufferedimage;
        }
    }

    public static BufferedImage rotateImageBy90(BufferedImage bufferedimage) {
        int h = bufferedimage.getHeight();
        int w = bufferedimage.getWidth();
        AffineTransform xform = new AffineTransform();
        xform.translate(0.5 * h, 0.5 * w);
        xform.rotate(Math.toRadians(90));
        xform.translate(-0.5 * w, -0.5 * h);
        AffineTransformOp op = new AffineTransformOp(xform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage bi = op.filter(bufferedimage, null);
        return bi;
    }

    /***
     * 读取文件流，获取metadata,检测orentation 字段值
     * @param instream
     * @return
     */
    public static int getOrientation(InputStream instream){
        int orientation = 1;
        if(instream != null){
            Metadata metadata;
            try {
                metadata = ImageMetadataReader.readMetadata(instream);
                Iterable<Directory> metaStr = metadata.getDirectories();
                for (Iterator<Directory> directoryIt = metaStr.iterator(); directoryIt.hasNext();) {
                    Directory directory = directoryIt.next();
                    if (directory.getClass().equals(ExifIFD0Directory.class)) {
                        Object obj = directory.getObject(274);
                        if(obj != null){
                            orientation = (int)obj;
                            break;
                        }
                    }
                }
            }catch(IOException e){
                logger.debug(e.getMessage());
            }catch(ImageProcessingException  e){
                logger.debug(e.getMessage());
            }
        }

        return orientation;
    }

    public static InputStream getRotatedImageStream(InputStream instream, int orientation){
        InputStream is = null;
        try {
            // read network image into buffer
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = instream.read(b)) != -1) {
                buf.write(b, 0, len);
            }

            instream.close();

            ByteArrayInputStream in = new ByteArrayInputStream(
                    buf.toByteArray());
            BufferedImage bf = ImageIO.read(in);
            in.close();

            if (bf != null) {
                bf = rotateImage(bf, getRotateDegree(orientation));
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
                ImageIO.write(bf, "png", imOut);
                is = new ByteArrayInputStream(bs.toByteArray());
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return is;
    }
}
