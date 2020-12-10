'use strict';


const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class Application extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            x: 0,
            y: 0
        };

        this.onMouseMove = this.onMouseMove.bind(this);
    }

    onMouseMove(e) {
        this.setState({ x: e.nativeEvent.offsetX, y: e.nativeEvent.offsetY });
    }

    componentDidMount() {
        client({ method: 'GET', path: '/api/countries' }).done(response => {
            this.setState({ countries: response.entity._embedded.countries });
        });
    }



    render() {
        const x = this.state.x;
        const y = this.state.y;

        console.log(this.state.countries);
        console.log(this.state.x);
        return (
            // https://stackoverflow.com/questions/42182481/getting-mouse-coordinates-in-react-and-jquery
            <div ref="elem" className="container">
                <div>
                    <img onMouseMove={this.onMouseMove} alt="map of the world" src="https://www.freeworldmaps.net/outline/maps/world-map-outline.gif" width="908" height="455" />
                </div>
                <h1>Mouse coordinates: {x} {y}</h1>
            </div>
        )
    }
}



class CountryList extends React.Component {
    render() {
        const countries = this.props.countries.map(country =>
            <Country key={country._links.self.href} country={country} />
        );
        return (
            <table>
                <tbody>
                    <tr>
                        <th>
                            Country Name
                        </th>
                        <th>
                            Land Area
                        </th>
                    </tr>
                    {countries}
                </tbody>
            </table>
        )
    }
}

class Country extends React.Component {
    render() {
        return (
            <tr>
                <td>
                    {this.props.country.name}
                </td>
                <td>
                    {this.props.country.landArea}
                </td>
            </tr>
        )
    }
}

ReactDOM.render(
    <Application />,
    document.getElementById('react')
);

const element = <h3>This is a test!</h3>;
ReactDOM.render(
    element,
    document.getElementById('test')
);
