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
    public partial class StoryPage : ContentPage
    {
        protected override void OnAppearing()
        {
            base.OnAppearing();
            //your code here;
            showMessage();
        }

        public async void showMessage() {
            await DisplayAlert("Info", "Klik op een foto om een bijhorende tekst te bekijken", "Ik heb het begrepen");
        }

        public StoryPage()
        {
            NavigationPage.SetHasBackButton(this, false);

            //var stack = new StackClickable();
            var list = new ListView();
            
            //LAYOUT

            var imageStyle = new Style(typeof(Image))
            {
                Setters = {
                    new Setter { Property = Image.MarginProperty, Value = new Thickness (50,0,50,0) }
                }
            };

            var labelStyle = new Style(typeof(Label))
            {
                Setters = {
                    new Setter { Property = Label.BackgroundColorProperty, Value = Color.FromHex("#372c73")},
                    new Setter { Property = Label.TextColorProperty, Value = Color.White},
                    new Setter { Property = Label.FontSizeProperty, Value=35 },
                    new Setter { Property = Label.HorizontalTextAlignmentProperty, Value=TextAlignment.Center}
                }
            };

            var frameStyle = new Style(typeof(Frame))
            {
                Setters = {
                    new Setter { Property =Frame.CornerRadiusProperty, Value = 25 },
                    new Setter { Property =Frame.PaddingProperty, Value = 0 },
                    new Setter { Property =Frame.MarginProperty, Value= new Thickness (50,10,50,50)},
                    new Setter { Property = Frame.IsVisibleProperty, Value=false},
                    new Setter { Property = Frame.HasShadowProperty, Value=true}
                   
                }
            };

            var stackLayout = new Style(typeof(StackClickable))
            {
                Setters = {
                    new Setter { Property = StackLayout.MarginProperty, Value = new Thickness(0,20,0,20)},
                    new Setter { Property = StackLayout.BackgroundColorProperty, Value = Color.LightGray}
                }
            };

            var scrollLayout = new Style(typeof(ScrollView))
            {
                Setters = {
                    new Setter { Property = ScrollView.BackgroundColorProperty, Value = Color.LightGray}
                }
            };

            Resources = new ResourceDictionary();
            Resources.Add(imageStyle);
            Resources.Add(labelStyle);
            Resources.Add(frameStyle);
            Resources.Add(stackLayout);
            Resources.Add(scrollLayout);
            var scroll = new ScrollView();
            var stacklayout = new StackLayout();
            var title = new Label { Text = "StoryBook", FontSize = 75, HorizontalTextAlignment = TextAlignment.Center };
            var titleFrame = new Frame { Content = title, IsVisible=true, Margin = 10 };
            stacklayout.Children.Add(titleFrame);



            for (int i = 0; i < 20; i++)
            {
                var stack = new StackClickable();
                var img = new Image { Source = "eighty.png" };
                var label = new Label { Text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." };
                var frame = new Frame { Content = label};
                stack.Children.Add(img);
                stack.Children.Add(frame);
                stacklayout.Children.Add(stack);
            }
            Content = new ScrollView { Content = stacklayout };
           
            

        }


    }



    public class StackClickable : StackLayout
    {
        Boolean clicked=false;

        public StackClickable()
        {
            TapGestureRecognizer singleTap = new TapGestureRecognizer()
            {
                NumberOfTapsRequired = 1
            };
            this.GestureRecognizers.Add(singleTap);
            singleTap.Tapped += stack_Clicked;
        }

        private void stack_Clicked(object sender, EventArgs e)
        {

            if (Children.Last().IsVisible)
            {
                Children.Last().IsVisible = false;
            }
            else {
                Children.Last().IsVisible = true;
            }
            
        }
    }
}