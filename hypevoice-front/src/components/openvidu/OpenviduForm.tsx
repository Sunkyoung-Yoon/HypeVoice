import React from 'react';

interface FormProps {
	createSession: () => void;
	joinSession: () => void;
	studioId: string;
	studioIdHandler: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

function OpenviduForm({
	createSession,
	joinSession,
	studioId,
	studioIdHandler,
}: FormProps) {
	const onCreateHandler = () => {
		createSession();
	};
	const onJoinHandler = (event: React.FormEvent) => {
		event.preventDefault();
		joinSession();
	};

	return (
		<>
			<h1>Create a video session</h1>
			<form onSubmit={onCreateHandler}>
				<p>
					<input type="submit" value="CREATE" />
				</p>
			</form>
			<h1>Join a video session</h1>
			<form onSubmit={onJoinHandler}>
				<p>
					<input
						type="text"
						value={studioId}
						onChange={studioIdHandler}
						maxLength={8}
						required
					/>
				</p>
				<p>
					<input type="submit" value="JOIN" />
				</p>
			</form>
		</>
	);
}

export default OpenviduForm;
