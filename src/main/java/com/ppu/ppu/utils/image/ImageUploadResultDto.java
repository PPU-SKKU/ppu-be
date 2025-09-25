package com.ppu.ppu.utils.image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
public class ImageUploadResultDto {
    String objectKey;
    String contentType;
    Dimension size;
}
