import React from 'react';
import {Scatter} from 'react-chartjs-2';

//const rand = () => Math.round(Math.random() * 20 - 10);
const rand = () => Math.random() * (1 - 0) + 0;
const data = {
  datasets: [
    {
      label: 'PSI',
      data: [
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
        { x: rand(), y: rand() },
      ],
      backgroundColor: 'rgba(255, 99, 132, 1)',
    },
  ],
};


class ScatterExample extends React.Component {

    render() {
        return (
            <div>
              <Scatter data={data} options={{
                onClick: function(evt, element, legend) {

                  if(element.length >0){

                    var ind = element[0]._index;
                    console.log(data.datasets[0].label);
                    
                    if(confirm(data.datasets[0].data[ind].x + ' ' + data.datasets[0].data[ind].y)){

                    }
                  }
                }
              }}/>
            </div>
        )
    }
}

export default ScatterExample;
