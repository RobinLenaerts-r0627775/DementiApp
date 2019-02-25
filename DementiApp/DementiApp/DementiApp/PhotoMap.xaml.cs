using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DementiApp
{
	[XamlCompilation(XamlCompilationOptions.Compile)]
	public partial class PhotoMap : ContentPage
	{
       private List<String> titlesArrayList = new List<String>();

		public PhotoMap ()
		{
			InitializeComponent ();


            var labelStyle = new Style(typeof(Label))
            {
                Setters = {
                    new Setter { Property = Label.BackgroundColorProperty, Value = Color.FromHex("#372c73")},
                    new Setter { Property = Label.TextColorProperty, Value = Color.White},
                    new Setter { Property = Label.FontSizeProperty, Value=35 },
                    new Setter { Property = Label.HorizontalTextAlignmentProperty, Value=TextAlignment.Center},
                    new Setter {Property = Label.WidthRequestProperty, Value=500},
                    new Setter {Property = Label.HeightRequestProperty, Value=100}
                }
            };

            var frameStyle = new Style(typeof(Frame))
            {
                Setters = {
                    new Setter { Property =Frame.CornerRadiusProperty, Value = 25 },
                    new Setter { Property =Frame.PaddingProperty, Value = 0 },
                    new Setter { Property =Frame.MarginProperty, Value= new Thickness (10,10,10,10)},
                    new Setter { Property = Frame.IsVisibleProperty, Value=true},
                    new Setter { Property = Frame.HasShadowProperty, Value=true},
                    new Setter { Property = Frame.HeightRequestProperty, Value=100}

                }
            };

            var gridStyle = new Style(typeof(RowDefinition))
            {
                Setters = {
                    new Setter { Property = RowDefinition.HeightProperty, Value=100 }
                }
            };


            Resources = new ResourceDictionary();
            Resources.Add(labelStyle);
            Resources.Add(frameStyle);
            Resources.Add(gridStyle);

            titlesArrayList = new List<String>();

            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            /*titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("Mocca");
            titlesArrayList.Add("loca");
            titlesArrayList.Add("loca");*/


            var rows =Math.Ceiling((double)(titlesArrayList.Count / 3));
            var titleIndex = 0;

            var gridLayout = new Grid();

            for (int rowInd = 0; rowInd <= rows; rowInd++) {

                gridLayout.RowDefinitions.Add(new RowDefinition());

            }

            for (int colInd = 0; colInd < 3; colInd++)
            {

                gridLayout.ColumnDefinitions.Add(new ColumnDefinition());

            }

            for (int rowIndex = 0; rowIndex <= rows; rowIndex++)
            {
                for (int columnIndex = 0; columnIndex < 3; columnIndex++)
                {
                    if (titleIndex >= titlesArrayList.Count)
                    {
                        break;
                    }
                    var product = titlesArrayList[titleIndex];
                    titleIndex += 1;
                    var label = new Label
                    {
                        Text = product,
                        VerticalOptions = LayoutOptions.Center,
                        HorizontalOptions = LayoutOptions.Center
                    };

                    var frame = new Frame { Content = label };
                    gridLayout.Children.Add(frame, columnIndex, rowIndex);
                }
            }
            var scrollLayout = new ScrollView { Content = gridLayout };
            Content = scrollLayout;
        }
	}
}