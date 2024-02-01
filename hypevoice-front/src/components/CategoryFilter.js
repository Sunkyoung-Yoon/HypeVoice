import React from "react";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import { pink } from "@mui/material/colors";
import Checkbox from "@mui/material/Checkbox";

function CategoryFilter() {
  return (
    <>
      <FormGroup>
        <FormControlLabel control={<Checkbox />} label="" />
        <FormControlLabel />
        <FormControlLabel />
      </FormGroup>
    </>
  );
}

export default CategoryFilter;
