package ru.practicum.explorewithme.event.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JsonTest
public class DtoJsonTest {

    @Autowired
    private JacksonTester<CreateEventDto> jsonTester;

    @Test
    public void testDeserializeDto() {

        String input = "{\"annotation\":\"annotation169119129619612961691616161651651\"," +
                "\"category\":6," +
                "\"description\":\"description9498465165165161616516516516516\"," +
                "\"eventDate\":\"2095-09-06 13:30:38\"," +
                "\"location\":{\"lat\":10,\"lon\":580}," +
                "\"paid\":true," +
                "\"participantLimit\":0," +
                "\"requestModeration\":true," +
                "\"title\":\"title\"}";

        try {
            CreateEventDto dto = jsonTester.parseObject(input);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
