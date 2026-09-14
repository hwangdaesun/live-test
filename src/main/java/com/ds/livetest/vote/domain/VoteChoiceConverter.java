package com.ds.livetest.vote.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
class VoteChoiceConverter implements AttributeConverter<VoteChoice, String> {

  @Override
  public String convertToDatabaseColumn(VoteChoice attribute) {
    return attribute == null ? null : attribute.value();
  }

  @Override
  public VoteChoice convertToEntityAttribute(String dbData) {
    return dbData == null ? null : VoteChoice.from(dbData);
  }
}
