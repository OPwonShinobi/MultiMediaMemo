package com.example.multimediamemos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemoEntry {
    private String mediaPath;
    private String caption;
    private String voiceRecordPath;
    private int id;
}
