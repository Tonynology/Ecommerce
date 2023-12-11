package project.Ecommerce.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryType {
  FASHION("패션"),
  FOOD("음식"),
  TOOLS("문구"),
  BEAUTY("뷰티"),
  PETS("애견"),
  ;
  private final String categoryKor;
}
