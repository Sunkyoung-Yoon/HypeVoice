import { CircularProgress } from '@mui/material';
import styled from 'styled-components';

const LoadingStyleDiv = styled.div`
	.loading-container {
		display: flex;
		justify-content: center;
		align-content: center;
		height: 100vh;
	}

	.loading-circle {
		margin-top: 10vh;
	}

	/* .loading-message {
		margin-top: 12vh;
	} */
`;

const LoadingComponent = () => {
	return (
		<LoadingStyleDiv>
			<div className="loading-container">
				<CircularProgress
					className="loading-circle"
					color="secondary"
					variant="indeterminate"
				/>
				{/* <div className="loading-message">Loading...</div> */}
			</div>
		</LoadingStyleDiv>
	);
};

export default LoadingComponent;
