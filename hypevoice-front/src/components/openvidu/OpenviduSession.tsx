import React from 'react';
import { useState, useEffect } from 'react';
import { Publisher, Subscriber } from 'openvidu-browser';
import OpenviduVideo from './OpenviduVideo';

interface SessionProps {
	subscriber: Subscriber;
	publisher: Publisher;
}

function OpenviduSession({ subscriber, publisher }: SessionProps) {
	const [subscribers, setSubscribers] = useState<Subscriber[]>([]);

	useEffect(() => {
		if (subscriber) {
			setSubscribers((prevSubscribers) => [...prevSubscribers, subscriber]);
		}
	}, [subscriber]);

	const adjustGridPlacement = (subscriberCount: number) => {
		if (subscriberCount <= 1) {
			return 'center';
		}
		return 'normal';
	};

	const renderSubscribers = () => {
		const gridPlacement = adjustGridPlacement(subscribers.length);

		return (
			<div
				style={{
					display: 'grid',
					gridTemplateColumns: gridPlacement === 'center' ? '1fr' : '1fr 1fr',
					gap: '20px',
				}}
			>
				<div>
					<OpenviduVideo streamManager={publisher} />
				</div>
				{subscribers.map((subscriberItem) => (
					<div key={subscriberItem.id}>
						<OpenviduVideo streamManager={subscriberItem} />
					</div>
				))}
			</div>
		);
	};

	return <>{renderSubscribers()}</>;
}

export default OpenviduSession;
