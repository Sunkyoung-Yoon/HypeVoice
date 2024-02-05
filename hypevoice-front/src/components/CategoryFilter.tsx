import React, { useState } from "react";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import { categories } from "./Category";
import { Box, Button, FormControl, FormLabel } from "@mui/material";
import { useRecoilState } from "recoil";
import ageGroupIcon from "../assets/ageGroupIcon.svg";
import mediaIcon from "../assets/mediaIcon.svg";
import sexIcon from "../assets/sexIcon.svg";
import styleIcon from "../assets/styleIcon.svg";
import toneIcon from "../assets/toneIcon.svg";
import { VoiceFilterCheckAtom } from "../recoil/VoiceFilterCheck";

// 분류 타입
// Recoil에 정의된 상태 중 key만을 가져옴
export type IconKey = keyof typeof categories;

// 분류 별 아이콘
// 분류 타입과 아이콘을 나타내는 문자열을 가지는 Record 타입
// 문자열 == 아이콘 이미지의 주소
const icons: Record<IconKey, string> = {
  미디어: mediaIcon,
  목소리톤: toneIcon,
  목소리스타일: styleIcon,
  성별: sexIcon,
  연령: ageGroupIcon,
};

// 각 분류에 맞는 아이콘을 반환 하는 함수
export function SmallIcon({ which }: { which: IconKey }) {
  const Icon = icons[which];
  if (!Icon) return null;
  // 최종적으로 src에 해당 아이콘에 해당하는 이미지 주소가 들어감
  return <img src={Icon} width="20px" height="20px" />;
}

type VoiceFilterCheckState = {
  [K in IconKey]: Record<string, boolean>;
};

type CategoryFilterProps = {
  onCheckChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
};

export default function CategoryFilter({
  onCheckChange,
  onConfirm,
}: CategoryFilterProps) {
  const [filterState, setFilterState] = useRecoilState(VoiceFilterCheckAtom);
  // 카테고리 선택 박스에서 '확인' 버튼을 눌렀을 때
  const handleConfirm = () => {
    onConfirm(filterState);
  };

  // 이전에 골라진 것들을 복사해두고, 그 중에 카테고리 별로 값들을 새로 체크된 형태로
  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const [category, value] = event.target.name.split("_");
    onCheckChange(event);

    setFilterState((prevState) => ({
      ...prevState,
      [category as keyof VoiceFilterCheckState]: {
        ...(prevState[category as IconKey] || {}),
        [value]: event.target.checked,
      },
    }));
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "flex-start",
        // border: "1px solid black",
        // borderRadius: "10px",
        padding: "10px",
      }}
    >
      {Object.entries(categories).map(([category, values]) => (
        // category == 분류명 // values == 선택지
        // 폼 전체 == FormControl
        // 분류명 == FormLabel
        // 선택지 그룹 == FormGroup
        // 선택지 == FormControlLabel
        <FormControl component="fieldset" key={category}>
          <div
            style={{
              display: "flex",
              alignItems: "center",
              alignContent: "center",
            }}
          >
            <SmallIcon which={category as IconKey} />
            <FormLabel
              component="legend"
              style={{
                textAlign: "left",
                fontWeight: "bold",
                fontSize: "18px",
                marginLeft: "5px",
                marginTop: "10px",
              }}
            >
              {category /* 카테고리명 ex : 미디어 */}
            </FormLabel>
            {/* <div onClick={handleCategoryCheck}>
              <FormHelperText> 전체 선택</FormHelperText>
            </div> */}
          </div>
          <FormGroup
            sx={{
              display: "flex",
              flexDirection: "row",
              flexWrap: "wrap",
              justifyContent: "flex-start",
            }}
          >
            {/* 선택지들로 배열을 만들건데 checked는 전역으로 관리되는 선택 여부고 이름은 카테고리명_옵션명으로 관리 */}
            {values.map((value) => (
              <FormControlLabel
                control={
                  <Checkbox
                    checked={filterState[category as IconKey]?.[value] || false}
                    onChange={handleChange}
                    name={`${category}_${value}`}
                  />
                }
                // 보여지는 이름
                label={value}
                // 실제 값
                key={value}
              />
            ))}
          </FormGroup>
          <hr />
        </FormControl>
      ))}
      <Button onClick={handleConfirm}>확인</Button>
    </Box>
  );
}
