package com.epam.gymapp.config;

import com.epam.gymapp.entities.Specialization;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SpecializationConverter implements Converter<String, Specialization> {
  @Override
  public Specialization convert(String source) {
    return Specialization.of(source);
  }
}
