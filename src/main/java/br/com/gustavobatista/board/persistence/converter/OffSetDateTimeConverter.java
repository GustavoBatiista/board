package br.com.gustavobatista.board.persistence.converter;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

import java.time.ZoneOffset;
import java.time.OffsetDateTime;
import java.sql.Timestamp;

@NoArgsConstructor(access = PRIVATE)
public final class OffSetDateTimeConverter {

    public static OffsetDateTime toOffsetDateTime(final Timestamp value) {
          return OffsetDateTime.ofInstant(value.toInstant(), ZoneOffset.UTC);
    }
}