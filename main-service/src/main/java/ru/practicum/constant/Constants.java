package ru.practicum.constant;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
public class Constants {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final Sort SORT_DESC_ID = Sort.by(Sort.Direction.DESC, "id");
    public static final Sort SORT_ASC_ID = Sort.by(Sort.Direction.ASC, "id");
    public static final Sort SORT_ASC_EVENT_DATE = Sort.by(Sort.Direction.ASC, "eventDate");
    public static final Sort SORT_DESC_VIEWS = Sort.by(Sort.Direction.DESC, "views");

}
