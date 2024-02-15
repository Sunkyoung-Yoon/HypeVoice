import styled from 'styled-components';
import ScrollToTopComponent from './ScrollToTopComponent';

const CenterStyleDiv = styled.div`
  width: 100%;
  background-color: white;
  // margin: auto;
  // overflow: auto;
`;

function Center({ children }) {
	ScrollToTopComponent();
	return <CenterStyleDiv>{children}</CenterStyleDiv>;
}

export default Center;
