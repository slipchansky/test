package com.upsic.kkc.sk11.data;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UdgData {

    private LocalDateTime dtStart; //Стартовая точка графика
    private LocalDateTime dtEnd; //Последняя точка графика
    private UUID objGen; //Идентификатор объекта
    private List<Float> values; //Значения точек графика (индекс - номер точки, объект - значение параметра). Ежеминутные значения
    private boolean deleteFlag; //Флаг удаления значений. Если флаг установлен - не образаем внимание на points и удаляем ежеминутные значения от dtStart до dtEnd

}
