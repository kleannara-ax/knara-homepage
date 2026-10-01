package com.kleannara.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MailModel {
    private String from;
    private String address;
    private String[] ccAddress;
    private String title;
    private String content;
    private String template;
    private Map<String, Object> variables;
}
