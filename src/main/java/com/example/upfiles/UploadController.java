package com.example.upfiles;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

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


}
