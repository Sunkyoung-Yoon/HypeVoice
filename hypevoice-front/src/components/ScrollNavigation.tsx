import React from "react";
import { Link, animateScroll } from "react-scroll";
import styled from "styled-components";

const FloatingScrollNavigation = styled.nav`
  position: fixed;
  top: 50%;
  left: 20px;
  text-align: center;

  p {
    width: 100px;
    padding: 5px 0px 5px 0px;
    margin: 0 auto;
  }

  .link {
    display: block;
    cursor: pointer;
    color: #5B5FF4;

    &:hover {
        background-color: lightgray;
    }
  }
`;

function ScrollNavigation() {
  return (
    <FloatingScrollNavigation>
      <p>
        <Link
          className="link"
          activeClass="active"
          to="top"
          spy={true}
          // smooth={true}
          offset={-70}
          duration={500}
        >
          ▲
        </Link>
      </p>
      <p>
              <Link
                  className="link"
          activeClass="active"
          to="work"
          spy={true}
          // smooth={true}
          offset={-70}
          duration={500}
        >
          작업물
        </Link>
      </p>
      <p>
              <Link
                  className="link"
          activeClass="active"
          to="addInfo"
          spy={true}
          // smooth={true}
          offset={-70}
          duration={500}
        >
          추가 정보
        </Link>
      </p>
      <p>
              <Link
                  className="link"
          activeClass="active"
          to="bottom"
          spy={true}
          // smooth={true}
          offset={-70}
          duration={500}
        >
          ▼
        </Link>
      </p>
    </FloatingScrollNavigation>
  );
}

export default ScrollNavigation;
