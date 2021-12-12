import React from 'react';
import {Bar} from 'react-chartjs-2';

const data = {
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [
        {
            label: 'Live Data',
            backgroundColor: 'rgba(255,99,132,0.2)',
            borderColor: 'rgba(255,99,132,1)',
            borderWidth: 1,
            hoverBackgroundColor: 'rgba(255,99,132,0.4)',
            hoverBorderColor: 'rgba(255,99,132,1)',
            borderCapStyle: 'round',
            data: [65, 59, 80, 81, 56, 55, 43]
        },
        {
            label: 'Train Data',
            backgroundColor: "#8e5ea2",
            borderColor: "#8e5ea2",
            borderWidth: 1,
            hoverBackgroundColor: 'rgba(255,99,132,0.4)',
            hoverBorderColor: 'rgba(255,99,132,1)',
            borderCapStyle: 'round',
            data: [70, 45, 61, 80, 58, 54, 41]
        }
    ]
};

class BarExample extends React.Component {

    render() {
        return (
            <div>
                <Bar
                    data={data}
                    width={100}
                    height={50}
                    options={{
                        maintainAspectRatio: true
                    }}
                />
            </div>
        )
    }
}

export default BarExample;