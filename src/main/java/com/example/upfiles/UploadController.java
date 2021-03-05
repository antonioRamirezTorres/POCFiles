package com.example.upfiles;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class UploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        Logger logger = LoggerFactory.getLogger(UploadController.class);

        Reader reader = new InputStreamReader(file.getInputStream());

        List<Modelo> beans = new CsvToBeanBuilder(reader)
                .withType(Modelo.class)
                .withSeparator(',')
                .build()
                .parse();

        logger.info(beans.toString());

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @GetMapping(value = "/getzip", produces="application/zip")
    public ResponseEntity<InputStreamResource> getZip() throws IOException {

        //ficheros a empaquetar, deberia ser un listado
        List<String> srcFiles = Arrays.asList(
                ResourceUtils.getURL("classpath:datos/pruebacsv.csv").getPath(),
                ResourceUtils.getURL("classpath:datos/pruebacsv2.txt").getPath());
        //nombre del fichero a comprimir
        FileOutputStream fos = new FileOutputStream("multiCompressed.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        //recorremos cada fichero y lo añadimos al zip
        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();

        //recuperamos el archivo zip y lo marcamos para la salida
        File file = new File(ResourceUtils.getURL("multiCompressed.zip")
                .getPath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentLength(file.length()) //
                .body(resource);

    }

}
