package com.event_manager_bot.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Основное его назначение — в объектах этого класса мы будет хранить результат парсинга текста.
//        Содержаться в нем будет только сама команда и весь текст, который идет после команды.

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParsedCommand {

    Command command = Command.NONE;
    String text = "";

}
